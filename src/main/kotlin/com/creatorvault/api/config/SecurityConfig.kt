package com.creatorvault.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter


@Configuration
class SecurityConfig(
    @Value("\${security.apiKey:dev-key}") private val apiKey: String
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/videos/**").permitAll()
                    .requestMatchers("/highlights/**").permitAll()
                    .anyRequest().authenticated()
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
            chain.doFilter(req, res)
        } else {
            println("❌ [ApiKeyFilter] Unauthorized — key mismatch or missing")
            res.status = 401
            res.writer.write("Unauthorized")
        }
    }
}
