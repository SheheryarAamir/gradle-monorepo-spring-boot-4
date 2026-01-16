package com.example.kafkaconsumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(GlobalTestConfig::class) // Only need this once here
abstract class BaseIntegrationTest

@TestConfiguration
class GlobalTestConfig {
    @Bean
    fun jackson2ObjectMapper(): ObjectMapper = ObjectMapper().apply {
        findAndRegisterModules()
    }
}
