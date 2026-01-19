package com.example.appservice.badssl

import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.springframework.boot.ssl.SslBundles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
class BadSslConfig(private val sslBundles: SslBundles) {

    @Bean
    fun badSslRestClient(
        builder: RestClient.Builder,
        authService: BadSslAuthService,
    ): RestClient {
        // Apply the mTLS bundle to the request factory
        val bundle = sslBundles.getBundle("bad-ssl-bundle")
        val factory = HttpComponentsClientHttpRequestFactory(
            HttpClients.custom()
                .setConnectionManager(
                    PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(SSLConnectionSocketFactory(bundle.createSslContext()))
                        .build(),
                ).build(),
        )

        return builder
            .requestFactory(factory)
            .requestInterceptor(BadSslAuthInterceptor(authService))
            .build()
    }
}
