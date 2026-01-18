package com.example.appservice.asyncapi

import com.example.appservice.BaseIntegrationTest
import io.github.springwolf.asyncapi.v3.jackson.DefaultAsyncApiSerializerService
import io.github.springwolf.core.asyncapi.AsyncApiService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@EmbeddedKafka(
    partitions = 1,
    bootstrapServersProperty = "spring.kafka.bootstrap-servers",
)
class AsyncApiGeneratorTest : BaseIntegrationTest() {

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.admin.fail-fast") { true }
            registry.add("spring.cloud.stream.kafka.binder.zkNodes") { "false" }
        }
    }

    @Autowired
    lateinit var asyncApiService: AsyncApiService

    @Test
    fun generateAsyncApiJson() {
        val asyncApi = asyncApiService.asyncAPI
        val json = DefaultAsyncApiSerializerService().toJsonString(asyncApi)

        java.io.File("../docs/app-service/asyncapi.json").apply {
            parentFile.mkdirs()
            writeText(json)
        }
    }
}
