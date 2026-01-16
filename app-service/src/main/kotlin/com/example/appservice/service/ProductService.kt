package com.example.appservice.service

import com.example.appservice.model.CreateProductRestModel
import com.example.events.ProductCreatedEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher
import kotlinx.coroutines.future.await
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(private val kafkaTemplate: KafkaTemplate<String, ProductCreatedEvent>) {
    private val logger = KotlinLogging.logger {}

    @AsyncPublisher(
        operation = AsyncOperation(
            channelName = "product-created-events-topic",
            description = "Publishes a product creation event with tracing context.",
            payloadType = ProductCreatedEvent::class,
            headers = AsyncOperation.Headers(
                schemaName = "TracingHeaders",
                values = [
                    AsyncOperation.Headers.Header(name = "traceparent", description = "W3C Trace Context header"),
                ],
            ),
        ),
    )
    suspend fun createProduct(productRestModel: CreateProductRestModel): String {
        val productId = UUID.randomUUID().toString()

        withLoggingContext("productId" to productId) {
            val productCreatedEvent = ProductCreatedEvent.newBuilder()
                .setProductId(productId)
                .setTitle(productRestModel.title)
                // You can pass the BigDecimal directly now!
                .setPrice(productRestModel.price)
                .setQuantity(productRestModel.quantity)
                .build()
            try {
                val result = kafkaTemplate.send(
                    "product-created-events-topic",
                    productId,
                    productCreatedEvent,
                ).await()
                withLoggingContext(
                    "partition" to result.recordMetadata.partition().toString(),
                    "topic" to result.recordMetadata.topic(),
                    "offset" to result.recordMetadata.offset().toString(),
                ) {
                    logger.info { "Message sent to product created event: ${result.recordMetadata}" }
                }
            } catch (e: Exception) {
                logger.error { "Product $productCreatedEvent could not be created - ${e.message}" }
                throw e
            }
            logger.info { "Return product created with id $productId" }
        }
        return productId
    }
}
