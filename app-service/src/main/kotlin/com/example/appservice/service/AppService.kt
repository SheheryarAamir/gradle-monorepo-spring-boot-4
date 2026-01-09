package com.example.appservice.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class AppService {

    fun getInfo() {
        logger.info { "Get info from service class" }
    }
}