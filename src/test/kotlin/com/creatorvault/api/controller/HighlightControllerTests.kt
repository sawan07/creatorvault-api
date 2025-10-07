package com.creatorvault.api.controller

import com.creatorvault.api.model.AnalyzeRequest
import com.creatorvault.api.model.Highlight
import com.creatorvault.api.model.HighlightsResponse
import com.creatorvault.api.service.HighlightServiceClient
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import reactor.core.publisher.Mono

@WebMvcTest(HighlightController::class)
@AutoConfigureMockMvc(addFilters = false)
class HighlightControllerTests(
    @Autowired val mockMvc: MockMvc
) {

    @MockBean
    lateinit var hiSvc: HighlightServiceClient

    @Test
    fun `should call analyze endpoint and return highlights`() {
        val req = AnalyzeRequest(videoId = "abc123", fileUrl = "https://cdn.test/video.mp4")
        val mockResponse = HighlightsResponse(
            videoId = "abc123",
            items = listOf(Highlight(0.0, 5.0, 0.9))
        )

        whenever(hiSvc.analyze(req)).thenReturn(Mono.just(mockResponse))

        mockMvc.post("/highlights/analyze") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"videoId":"abc123","fileUrl":"https://cdn.test/video.mp4"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.videoId") { value("abc123") }
            jsonPath("$.items[0].confidence") { value(0.9) }
        }
    }

    @Test
    fun `should get highlights for given video`() {
        val mockResponse = HighlightsResponse(
            videoId = "xyz789",
            items = listOf(Highlight(1.0, 3.5, 0.8))
        )

        whenever(hiSvc.get("xyz789")).thenReturn(Mono.just(mockResponse))

        mockMvc.get("/highlights/xyz789")
            .andExpect {
                status { isOk() }
                jsonPath("$.videoId") { value("xyz789") }
                jsonPath("$.items[0].startTime") { value(1.0) }
            }
    }
}
