package com.example.appservice.controller

import com.example.appservice.service.AppService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.withLoggingContext
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api", produces = [MediaType.APPLICATION_JSON_VALUE])
class AppController (
    private val appService: AppService
) {

    @GetMapping("/info")
    fun getBuildInfo() : ResponseEntity<String> {
        withLoggingContext("customerId" to "123", "orderId" to "xyz") {
            logger.info { "Processing hello request" }
            appService.getInfo()
        }
        return ResponseEntity.ok("Get build info, gradle monorepo")
    }
}