package com.example.kafkaconsumer.handler

import com.example.events.ProductCreatedEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.springwolf.core.asyncapi.annotations.AsyncListener
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics = ["product-created-events-topic"])
class ProductCreatedEventHandler {

    private val logger = KotlinLogging.logger {}

    @AsyncListener(
        operation = AsyncOperation(
            channelName = "product-created-events-topic",
            description = "Consumes product creation events and processes them.",
            headers = AsyncOperation.Headers(
                schemaName = "TracingHeaders",
                values = [
                    AsyncOperation.Headers.Header(name = "traceparent", description = "W3C Trace Context header"),
                ],
            ),
        ),
    )
    @KafkaHandler
    fun handle(productCreatedEvent: ProductCreatedEvent) {
        logger.info { "Product created event received: ${productCreatedEvent.productId}" }
    }
}
