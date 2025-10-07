package com.creatorvault.api.controller

import com.creatorvault.api.model.MeResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/me")
class AuthController {
    @GetMapping
    fun me(): ResponseEntity<MeResponse> = ResponseEntity.ok(
        MeResponse(email = "creator@example.com", channelId = null)
    )
}