package com.creatorvault.api.model

import java.time.LocalDateTime
import java.util.*

data class UserResponse(
    val id: UUID,
    val email: String,
    val createdAt: LocalDateTime
)
