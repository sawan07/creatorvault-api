package com.creatorvault.api.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(AuthController::class)
class AuthControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should return current user info`() {
        mockMvc.get("/me")
            .andExpect {
                status { isOk() }
                jsonPath("$.email") { value("creator@example.com") }
                jsonPath("$.channelId") { doesNotExist() }
            }
    }
}
