package com.example.appservice.badssl

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class BadSslAuthInterceptor(private val authService: BadSslAuthService) : ClientHttpRequestInterceptor {
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse {
        val token = authService.getAccessToken()
        request.headers.setBearerAuth(token)

        return execution.execute(request, body)
    }
}
