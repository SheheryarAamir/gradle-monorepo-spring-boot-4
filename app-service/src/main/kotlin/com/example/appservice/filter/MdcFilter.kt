package com.example.appservice.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class MdcFilter : OncePerRequestFilter() {
    companion object {
        private const val TRACE_ID_KEY = "traceId"
        private const val TRACE_ID_HEADER = "X-Trace-Id"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Get from header (for distributed tracing) or generate new
        val traceId = request.getHeader(TRACE_ID_HEADER) ?: UUID.randomUUID().toString()

        try {
            // 2. Push to MDC
            MDC.put(TRACE_ID_KEY, traceId)
            // 3. Add to response header so the client knows the ID for debugging
            response.addHeader(TRACE_ID_HEADER, traceId)

            filterChain.doFilter(request, response)
        } finally {
            // 4. ALWAYS clear to prevent ThreadLocal leaks
            MDC.remove(TRACE_ID_KEY)
        }
    }
}