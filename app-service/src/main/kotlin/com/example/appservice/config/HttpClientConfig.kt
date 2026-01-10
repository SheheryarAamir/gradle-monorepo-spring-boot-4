package com.example.appservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class HttpClientConfig {

    @Bean
    fun jsonPlaceholderClient(builder: RestClient.Builder): RestClient {
        return builder
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build()
    }
}