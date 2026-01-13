package com.example.appservice.service

import com.example.appservice.event.ProductCreatedEvent
import com.example.appservice.model.CreateProductRestModel
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.future.await
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(private val kafkaTemplate: KafkaTemplate<String, ProductCreatedEvent>) {
    private val logger = KotlinLogging.logger {}

    suspend fun createProduct(productRestModel: CreateProductRestModel): String {
        val productId = UUID.randomUUID().toString()

        val productCreatedEvent = ProductCreatedEvent(
            productId,
            productRestModel.title,
            productRestModel.price,
            productRestModel.quantity,
        )
        try {
            val result = kafkaTemplate.send(
                "product-created-events-topic",
                productId,
                productCreatedEvent,
            ).await()
            logger.info { "Message sent to product created event: ${result.recordMetadata}" }
        } catch (e: Exception) {
            logger.error { "Product $productCreatedEvent could not be created - ${e.message}" }
        }
        logger.info { "Return product created with id $productId" }
        return productId
    }
}
