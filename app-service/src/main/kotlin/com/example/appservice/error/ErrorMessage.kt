package com.example.appservice.error

import kotlin.time.Instant

data class ErrorMessage(val timestamp: Instant, val message: String, val details: String)
