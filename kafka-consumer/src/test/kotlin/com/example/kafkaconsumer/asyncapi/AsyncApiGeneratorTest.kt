package com.example.kafkaconsumer.asyncapi

import com.example.kafkaconsumer.BaseIntegrationTest
import io.github.springwolf.asyncapi.v3.jackson.DefaultAsyncApiSerializerService
import io.github.springwolf.core.asyncapi.AsyncApiService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AsyncApiGeneratorTest : BaseIntegrationTest() {

    @Autowired
    lateinit var asyncApiService: AsyncApiService

    @Test
    fun generateAsyncApiJson() {
        val asyncApi = asyncApiService.asyncAPI
        val json = DefaultAsyncApiSerializerService().toJsonString(asyncApi)

        java.io.File("../docs/kafka-consumer/asyncapi.json").apply {
            parentFile.mkdirs()
            writeText(json)
        }
    }
}
