package com.example.kafkaconsumer

import com.example.events.ProductCreatedEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics = ["product-created-events-topic"])
class ProductCreatedEventHandler {

    private val logger = KotlinLogging.logger {}

    @KafkaHandler
    fun handle(productCreatedEvent: ProductCreatedEvent) {
        logger.info { "Product created event received: ${productCreatedEvent.productId}" }
    }
}
