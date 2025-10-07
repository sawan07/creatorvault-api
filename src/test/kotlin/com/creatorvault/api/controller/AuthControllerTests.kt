package com.creatorvault.api.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests(
    @Autowired val mockMvc: MockMvc
) {
    @Test
    fun `should return current user info`() {
        mockMvc.get("/me")
            .andExpect {
                status { isOk() }
                jsonPath("$.email") { value("creator@example.com") }
            }
    }
}
