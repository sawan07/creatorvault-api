package com.creatorvault.api.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.web.reactive.function.client.WebClient

class YouTubeServiceTests {
    private val webClient = mock<WebClient>()
    private val service = YouTubeService(webClient, "fake-api-key")

    @Test
    fun `should parse ISO duration correctly`() {
        val result = service.run {
            // using reflection if needed or test helper
            javaClass.getDeclaredMethod("parseIsoDurationSeconds", String::class.java)
                .apply { isAccessible = true }
                .invoke(this, "PT1H2M10S") as Long
        }

        assertEquals(3730, result)
    }
}
