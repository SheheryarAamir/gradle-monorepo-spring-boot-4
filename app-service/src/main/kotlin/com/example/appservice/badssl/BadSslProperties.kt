package com.example.appservice.badssl

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bad-ssl")
data class BadSslProperties(
    val baseUrl: String,
    val username: String,
    val password: String,
    val sslBundleName: String = "bad-ssl-bundle",
)
