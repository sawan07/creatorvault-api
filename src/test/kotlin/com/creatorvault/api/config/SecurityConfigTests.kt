package com.creatorvault.api.config

import com.creatorvault.api.controller.VideoController
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
class SecurityConfigTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var yt: YouTubeService

    @MockBean
    lateinit var videoSvc: VideoServiceClient

    @Test
    fun `should allow GET requests to videos without authentication`() {
        // stub out the YouTubeService so controller returns something valid
        whenever(yt.listUploads(null, null)).thenReturn(emptyList())

        mockMvc.get("/videos")
            .andExpect {
                status { isOk() }
            }
    }
}
