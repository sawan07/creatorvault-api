package com.creatorvault.api.controller

import com.creatorvault.api.model.Highlight
import com.creatorvault.api.model.HighlightsResponse
import com.creatorvault.api.service.HighlightServiceClient
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import reactor.core.publisher.Mono

@WebMvcTest(HighlightController::class)
class HighlightControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var hiSvc: HighlightServiceClient

    @Test
    fun `should call analyze endpoint and return highlights`() {
        val mockResponse = HighlightsResponse(
            videoId = "abc123",
            items = listOf(
                Highlight(startTime = 10.0, endTime = 20.0, confidence = 0.95)
            )
        )

        whenever(hiSvc.analyze(any())).thenReturn(Mono.just(mockResponse))

        mockMvc.post("/highlights/analyze") {
            contentType = MediaType.APPLICATION_JSON
            // DTO expects videoId + fileUrl
            content = """{"videoId":"abc123","fileUrl":"https://cdn.test/video.mp4"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.videoId") { value("abc123") }
            jsonPath("$.items[0].startTime") { value(10.0) }
            jsonPath("$.items[0].endTime") { value(20.0) }
            jsonPath("$.items[0].confidence") { value(0.95) }
        }
    }

    @Test
    fun `should get highlights for given video`() {
        val mockResponse = HighlightsResponse(
            videoId = "xyz789",
            items = listOf(
                Highlight(startTime = 5.0, endTime = 15.0, confidence = 0.9)
            )
        )

        whenever(hiSvc.get("xyz789")).thenReturn(Mono.just(mockResponse))

        mockMvc.get("/highlights/xyz789")
            .andExpect {
                status { isOk() }
                jsonPath("$.videoId") { value("xyz789") }
                jsonPath("$.items[0].startTime") { value(5.0) }
                jsonPath("$.items[0].endTime") { value(15.0) }
                jsonPath("$.items[0].confidence") { value(0.9) }
            }
    }
}
