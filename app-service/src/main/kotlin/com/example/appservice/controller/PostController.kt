package com.example.appservice.controller

import com.example.appservice.service.PostDto
import com.example.appservice.service.PostService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/post", produces = [MediaType.APPLICATION_JSON_VALUE])
class PostController (private val postService: PostService) {

    @GetMapping()
    fun getPosts() : ResponseEntity<List<PostDto>> {
        val posts = postService.getPostsFromDB() ?: emptyList()
        return withLoggingContext( "postCount" to posts.size.toString()) {
            logger.info { "Processing Post request" }
            ResponseEntity.ok(posts)
        }
    }

}