package com.example.appservice.service

import com.example.appservice.entity.toDto
import com.example.appservice.repository.PostRepository
import io.micrometer.observation.annotation.Observed
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class PostService(private val restClient: RestClient,
    private val postRepository: PostRepository) {

    @Observed(name = "fetch-external-posts")
    fun getPosts(): List<PostDto>? {
        return restClient.get()
            .uri("/posts")
            .retrieve()
            .body(object : ParameterizedTypeReference<List<PostDto>>() {})
    }

    @Observed(name = "fetch-internal-posts")
    fun getPostsFromDB(): List<PostDto>? {
        return postRepository.findAll().map { it.toDto() }
    }
}

data class PostDto(val userId: String, val id: String, val title: String, val body: String)