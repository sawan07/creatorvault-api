package com.creatorvault.api.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigTests(
    @Autowired val webClientBuilder: WebClient.Builder,
    @LocalServerPort val port: Int
) {

    @Test
    fun `should allow GET requests to videos without authentication`() {
        val client = webClientBuilder.baseUrl("http://localhost:$port").build()
        val response = client.get().uri("/videos").retrieve().toBodilessEntity().block()
        assert(response?.statusCode?.is2xxSuccessful == true)
    }
}
