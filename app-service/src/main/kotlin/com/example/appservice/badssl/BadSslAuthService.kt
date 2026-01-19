package com.example.appservice.badssl

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class BadSslAuthService(private val props: BadSslProperties) {

    private val tokenCache = Caffeine.newBuilder()
        .expireAfterWrite(270, TimeUnit.SECONDS) // Refresh every 4.5 mins
        .build<String, String>()

    fun getAccessToken(): String = tokenCache.get("current_token") {
        println("Handshaking with BadSSL...")
        // In a real scenario, make a RestClient call here.
        // For BadSSL test, we just return a dummy token after a successful mTLS connection check.
        "mock-jwt-token-${System.currentTimeMillis()}"
    } ?: throw IllegalStateException("Auth failed")
}
