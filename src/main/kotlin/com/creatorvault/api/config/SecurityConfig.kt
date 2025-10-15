package com.creatorvault.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority


@Configuration
class SecurityConfig(
    @Value("\${security.apiKey:dev-key}") private val apiKey: String
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                // Allow all GETs to videos and actuator
                it.requestMatchers(HttpMethod.GET, "/actuator/**", "/videos/**").permitAll()

                // Allow POSTs to /videos/** if API key is valid (checked by filter)
                it.requestMatchers(HttpMethod.POST, "/videos/**").authenticated()

                // Default rule for all other endpoints
                it.anyRequest().authenticated()
            }
            .addFilterBefore(ApiKeyFilter(apiKey), BasicAuthenticationFilter::class.java)
            .httpBasic { it.disable() }
            .formLogin { it.disable() }

        println("✅ Custom SecurityConfig loaded successfully! (API key = $apiKey)")

        return http.build()
    }
}

class ApiKeyFilter(private val apiKey: String) : OncePerRequestFilter() {
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val provided = req.getHeader("X-API-Key") ?: req.getHeader("x-api-key")
        println("🔍 [ApiKeyFilter] ${req.method} ${req.requestURI} — X-API-Key: $provided")

        if (provided == apiKey) {
            println("✅ [ApiKeyFilter] API key validated")

            // Mark request as authenticated
            val auth = UsernamePasswordAuthenticationToken("api-key-user", null, listOf(SimpleGrantedAuthority("ROLE_API")))
            SecurityContextHolder.getContext().authentication = auth

            chain.doFilter(req, res)
        } else {
            println("❌ [ApiKeyFilter] Unauthorized — key mismatch or missing")
            res.status = 401
            res.writer.write("Unauthorized")
        }
    }
}

