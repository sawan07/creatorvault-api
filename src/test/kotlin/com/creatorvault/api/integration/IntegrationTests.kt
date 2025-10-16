package com.creatorvault.api.integration

import com.creatorvault.api.CreatorVaultApiApplication
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(
    classes = [CreatorVaultApiApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class IntegrationTests(
    @Autowired private val restTemplate: TestRestTemplate,
    @LocalServerPort private val port: Int
) {

    companion object {
        private lateinit var videoServer: MockWebServer
        private lateinit var highlightServer: MockWebServer

        @JvmStatic
        @BeforeAll
        fun setup() {
            videoServer = MockWebServer().apply { start() }
            highlightServer = MockWebServer().apply { start() }

            // Print ports to verify
            println("🎬 Video mock running on port: ${videoServer.port}")
            println("💡 Highlight mock running on port: ${highlightServer.port}")

            // Update environment for the running Spring app
            System.setProperty("services.video.baseUrl", "http://localhost:${videoServer.port}")
            System.setProperty("services.highlight.baseUrl", "http://localhost:${highlightServer.port}")
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProps(registry: DynamicPropertyRegistry) {
            registry.add("services.video.baseUrl") { "http://localhost:${videoServer.port}" }
            registry.add("services.highlight.baseUrl") { "http://localhost:${highlightServer.port}" }
            registry.add("security.apiKey") { "dev-key" }
        }

        @JvmStatic
        @AfterAll
        fun teardown() {
            videoServer.shutdown()
            highlightServer.shutdown()
        }
    }

    @Test
    fun `should call video-service download endpoint`() {
        videoServer.enqueue(
            MockResponse()
                .setBody("""{"jobId":"job123","status":"QUEUED"}""")
                .addHeader("Content-Type", "application/json")
        )

        val headers = HttpHeaders()
        headers["X-API-Key"] = "dev-key"
        headers.contentType = MediaType.APPLICATION_JSON

        // 👇 now we send the YouTube/video ID in the JSON body
        val body = """{"youtube_id": "abc123"}"""
        val entity = HttpEntity(body, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/videos/download",  // 👈 endpoint changed
            HttpMethod.POST,
            entity,
            String::class.java
        )

        println("🔍 Response status: ${response.statusCode}")
        println("🔍 Response body: ${response.body}")

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.contains("job123"))
    }


    @Test
    fun `should call highlight-service analyze endpoint`() {
        highlightServer.enqueue(MockResponse()
            .setBody("""{"videoId":"abc123","items":[{"startTime":1.0,"endTime":2.5,"confidence":0.9}]}""")
            .addHeader("Content-Type", "application/json")
        )

        val headers = HttpHeaders()
        headers["X-API-Key"] = "dev-key"
        headers.contentType = MediaType.APPLICATION_JSON
        val body = """{"videoId":"abc123","fileUrl":"https://cdn.test/video.mp4"}"""
        val entity = HttpEntity(body, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/highlights/analyze",
            HttpMethod.POST, entity, String::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.contains("confidence"))
    }
}
