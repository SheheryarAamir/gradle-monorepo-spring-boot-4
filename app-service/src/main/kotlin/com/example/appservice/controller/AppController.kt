package com.example.appservice.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api", produces = [MediaType.APPLICATION_JSON_VALUE])
class AppController {

    @GetMapping("/info")
    fun getBuildInfo() : ResponseEntity<String> {
        return ResponseEntity.ok("Get build info, gradle monorepo")
    }
}