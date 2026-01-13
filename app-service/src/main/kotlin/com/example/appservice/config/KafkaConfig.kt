package com.example.appservice.config

import com.example.appservice.event.ProductCreatedEvent
import io.micrometer.observation.ObservationRegistry
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation

@Configuration
class KafkaConfig {

    @Bean
    fun kafkaTemplate(
        producerFactory: ProducerFactory<String, ProductCreatedEvent>,
        observationRegistry: ObservationRegistry,
    ): KafkaTemplate<String, ProductCreatedEvent> {
        val template = KafkaTemplate(producerFactory)
        template.setObservationEnabled(true)
        template.setObservationConvention(KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention())
        return template
    }

    @Bean
    fun createTopic(): NewTopic = TopicBuilder
        .name("product-created-events-topic")
        .partitions(3)
        .replicas(1)
        .build()
}
