package com.creatorvault.api.util


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class HttpClientCfg {
    @Bean
    fun webClient(): WebClient = WebClient.builder().build()
}