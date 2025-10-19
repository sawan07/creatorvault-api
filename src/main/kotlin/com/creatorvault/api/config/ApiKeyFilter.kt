package com.creatorvault.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyFilter(
    @Value("\${security.apiKey:dev-key}") private val apiKey: String
) : OncePerRequestFilter() {

    companion object {
        private const val ROLE_API = "ROLE_API"
        private val log = LoggerFactory.getLogger(ApiKeyFilter::class.java)
        private const val API_KEY_HEADER = "X-API-Key"
    }

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val provided = req.getHeader(API_KEY_HEADER) ?: req.getHeader(API_KEY_HEADER.lowercase())

        // 💡 NEW LOGGING: Log both keys for comparison
        log.info("🔍 [ApiKeyFilter] Expected Key: '{}', Provided Key: '{}'", apiKey, provided)

        // 💡 NEW DEFENSIVE COMPARISON: Use safe call (provided?) and trim/equality check
        if (provided != null && provided.trim().equals(apiKey.trim())) {
            log.info("✅ [ApiKeyFilter] API key validated")

            val auth = UsernamePasswordAuthenticationToken(
                "api-client", null, listOf(SimpleGrantedAuthority(ROLE_API))
            )
            val context: SecurityContext = SecurityContextImpl(auth)

            try {
                // Manually set the context
                SecurityContextHolder.setContext(context)
                chain.doFilter(req, res)
            } finally {
                // Manually clear the context for stateless operation
                SecurityContextHolder.clearContext()
            }
        } else {
            // 💡 NEW LOGGING: Warn with the status
            log.warn("❌ [ApiKeyFilter] Unauthorized (401) — Key mismatch or missing.")
            res.status = HttpServletResponse.SC_UNAUTHORIZED
            res.contentType = "application/json"
            res.writer.write("{\"error\": \"Unauthorized\"}")
        }
    }
}