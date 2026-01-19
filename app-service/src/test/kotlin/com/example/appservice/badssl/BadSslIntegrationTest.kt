package com.example.appservice.badssl

import com.example.appservice.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.restclient.test.autoconfigure.AutoConfigureMockRestServiceServer
import org.springframework.http.MediaType
import org.springframework.test.web.client.ExpectedCount.once
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient

@AutoConfigureMockRestServiceServer
@EnableConfigurationProperties(BadSslProperties::class)
class BadSslIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var server: MockRestServiceServer

    private lateinit var restClient: RestClient

    @Autowired
    private lateinit var restClientBuilder: RestClient.Builder

    @Autowired
    lateinit var badSslProperties: BadSslProperties

    @BeforeEach
    fun setUp() {
        // 3. Manually bind the server to the specific builder
        server = MockRestServiceServer.bindTo(restClientBuilder).build()

        // Build the client from the now-mocked builder
        restClient = restClientBuilder.build()
    }

    @Test
    fun `verify mTLS call is intercepted`() {
        val testUri = "https://client.badssl.com/api/test"

        server.expect(once(), requestTo(testUri))
            .andRespond(withSuccess("""{"status":"ok"}""", MediaType.APPLICATION_JSON))

        val response = restClient.get()
            .uri(testUri)
            .retrieve()
            .body(String::class.java)

        server.verify()
        assertNotNull(response)
    }
}
