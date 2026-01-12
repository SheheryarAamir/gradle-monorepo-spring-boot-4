package com.example.appservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.observation.annotation.Observed
import org.springframework.stereotype.Service

@Service
class AppService(
    private val postService: PostService,
) {
    private val logger = KotlinLogging.logger {}
    @Observed(name = "get-service-info")
    fun getInfo() {
        postService.getPosts()
        logger.info { "Get info from service class" }
    }
}
