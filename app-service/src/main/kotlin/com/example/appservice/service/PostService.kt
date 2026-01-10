package com.example.appservice.service

import io.micrometer.observation.annotation.Observed
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class PostService(private val restClient: RestClient) {

    @Observed(name = "fetch-external-posts")
    fun getPosts(): List<Post>? {
        return restClient.get()
            .uri("/posts")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<Post>>() {})
    }
}

data class Post(val userId: String, val id: String, val title: String, val body: String)