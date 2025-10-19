package com.creatorvault.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.SecurityContextPersistenceFilter


@Configuration
class SecurityConfig(private val apiKeyFilter: ApiKeyFilter) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }

            // Fix: Enforce stateless session management for API
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            // Fix: Authorization chaining to resolve 'At least one mapping is required'
            .authorizeHttpRequests { auth ->
                auth.run {
                    requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    requestMatchers("/users/**").authenticated()
                    requestMatchers("/videos/**").authenticated()
                    requestMatchers("/highlights/**").authenticated()
                    anyRequest().denyAll()
                }
            }

            // Fix: Add filter BEFORE the context persistence filter
            .addFilterBefore(apiKeyFilter, SecurityContextPersistenceFilter::class.java)

        return http.build()
    }
}