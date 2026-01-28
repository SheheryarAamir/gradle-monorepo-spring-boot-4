package com.example.kafkaconsumer.handler

import com.example.events.ProductCreatedEvent
import com.example.kafkaconsumer.BaseIntegrationTest
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.timeout
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.kafka.autoconfigure.KafkaConnectionDetails
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import java.math.BigDecimal
import java.util.UUID

@SpringBootTest(
    properties = [
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.producer.value-serializer=io.confluent.kafka.serializers.KafkaAvroSerializer",
        "spring.kafka.producer.properties.schema.registry.url=mock://product-handler-scope",
        "spring.kafka.consumer.properties.schema.registry.url=mock://product-handler-scope",
        "spring.kafka.consumer.properties.specific.avro.reader=true",
    ],
)
@EmbeddedKafka(partitions = 3, topics = ["product-created-events-topic"])
class ProductCreatedEventHandlerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, ProductCreatedEvent>

    @MockitoSpyBean
    private lateinit var productCreatedEventHandler: ProductCreatedEventHandler

    @Test
    fun testProductCreatedEventHandler_OnProductCreated_HandlesEvent() {
        val productCreatedEvent = ProductCreatedEvent(
            UUID.randomUUID().toString(),
            "testTitle",
            BigDecimal(600),
            1,
        )

        val messageId = UUID.randomUUID().toString()
        val messageKey = productCreatedEvent.productId

        val record = ProducerRecord("product-created-events-topic", messageKey, productCreatedEvent)
        record.headers().add("messageId", messageId.toByteArray())
        record.headers().add(KafkaHeaders.RECEIVED_KEY, messageKey.toByteArray())

        kafkaTemplate.send(record)

        argumentCaptor<ProductCreatedEvent>().apply {
            verify(productCreatedEventHandler, timeout(5000).times(1))
                .handle(capture())
            assertEquals(productCreatedEvent.productId, firstValue.productId)
        }
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun kafkaServiceConnection(embeddedKafka: EmbeddedKafkaBroker): KafkaConnectionDetails = KafkaConnectionDetails {
            embeddedKafka.brokersAsString.split(",")
        }
    }
}
