package com.example.appservice.config

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class OtelLogInstaller(
    private val openTelemetry: OpenTelemetry,
) : InitializingBean {
    override fun afterPropertiesSet() {
        // This links the Logback appender to the Spring-managed OTel SDK
        OpenTelemetryAppender.install(openTelemetry)
    }
}
