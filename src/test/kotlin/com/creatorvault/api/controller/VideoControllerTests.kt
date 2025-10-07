package com.creatorvault.api.controller

import com.creatorvault.api.model.VideoItem
import com.creatorvault.api.service.YouTubeService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(VideoController::class)
class VideoControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var yt: YouTubeService

    @Test
    fun `should return list of videos`() {
        // Mock YouTubeService
        whenever(yt.listUploads(any(), any()))
            .thenReturn(
                listOf(
                    VideoItem(
                        youtubeId = "abc123",
                        title = "Sample Video",
                        durationSeconds = 120,
                        thumbnailUrl = null
                    )
                )
            )

        // Perform GET /videos
        mockMvc.get("/videos")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
            }
    }
}
