package com.creatorvault.api.controller

import com.creatorvault.api.model.VideoItem
import com.creatorvault.api.model.VideoListResponse
import com.creatorvault.api.service.VideoServiceClient
import com.creatorvault.api.service.YouTubeService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(VideoController::class)
@AutoConfigureMockMvc(addFilters = false)
class VideoControllerTests(
    @Autowired val mockMvc: MockMvc
) {

    @MockBean
    lateinit var videoServiceClient: VideoServiceClient

    @MockBean
    lateinit var youTubeService: YouTubeService

    @Test
    fun `should return list of videos`() {
        val mockVideos = listOf(
            VideoItem(
                youtubeId = "abc123",
                title = "Demo",
                durationSeconds = 120,
                thumbnailUrl = "https://i.ytimg.com/vi/abc123/hqdefault.jpg"
            )
        )

        // Mock the service response
        whenever(youTubeService.listUploads(googleToken = null, channelId = null))
            .thenReturn(mockVideos)


        mockMvc.get("/videos")
            .andExpect {
                status { isOk() }
                jsonPath("$.items[0].youtubeId") { value("abc123") }
                jsonPath("$.items[0].title") { value("Demo") }
            }
    }
}
