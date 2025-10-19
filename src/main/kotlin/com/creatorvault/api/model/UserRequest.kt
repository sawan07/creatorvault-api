package com.creatorvault.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    val email: String
)
