package com.example.appservice.service

import com.example.appservice.BaseIntegrationTest
import com.example.appservice.model.CreateProductRestModel
import com.example.events.ProductCreatedEvent
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.kafka.autoconfigure.KafkaConnectionDetails
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import java.math.BigDecimal
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.to

@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 3, controlledShutdown = true, topics = ["product-created-events-topic"])
@TestPropertySource(properties = ["spring.kafka.producer.properties.schema.registry.url=mock://product-service-scope"])
class ProductServiceIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Autowired
    private lateinit var environment: Environment

    private lateinit var container: KafkaMessageListenerContainer<String, ProductCreatedEvent>
    private lateinit var records: BlockingQueue<ConsumerRecord<String, ProductCreatedEvent>>

    @BeforeAll
    fun setUp() {
        val consumerFactory = DefaultKafkaConsumerFactory<String, ProductCreatedEvent>(getConsumerProps())
        val containerProperties = ContainerProperties("product-created-events-topic")
        container = KafkaMessageListenerContainer(consumerFactory, containerProperties)
        records = LinkedBlockingQueue()
        container.setupMessageListener(
            MessageListener<String, ProductCreatedEvent> { record ->
                records.add(record)
            },
        )
        container.start()
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.partitionsPerTopic)
    }

    @Test
    suspend fun testCreateProduct_whenGivenValidProductDetails_successfullSendsKafkaMessage() {
        val title = "testTitle"
        val price = BigDecimal(600)
        val quality = 1
        val createProductRestModel = CreateProductRestModel(title, price, quality)

        productService.createProduct(createProductRestModel)

        val msg = withContext(Dispatchers.IO) {
            records.poll(3000, TimeUnit.MILLISECONDS)
        }
        assertNotNull(msg)
        assertNotNull(msg?.key())
        val event = msg?.value()
        assertEquals(createProductRestModel.title, event?.title)
        assertThat(event?.price).isEqualByComparingTo(createProductRestModel.price)
        assertEquals(createProductRestModel.quantity, event?.quantity)
    }

    private fun getConsumerProps(): Map<String, Any> = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to embeddedKafkaBroker.brokersAsString,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java,
        ConsumerConfig.GROUP_ID_CONFIG to (environment.getProperty("spring.application.name") ?: "test-group"),
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        "schema.registry.url" to "mock://product-service-scope",
        KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG to true,
    )

    @AfterAll
    fun tearDown() {
        container.stop()
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun kafkaServiceConnection(embeddedKafka: EmbeddedKafkaBroker): KafkaConnectionDetails = KafkaConnectionDetails {
            embeddedKafka.brokersAsString.split(",")
        }
    }
}
