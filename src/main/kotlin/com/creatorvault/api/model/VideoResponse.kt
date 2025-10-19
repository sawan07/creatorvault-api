package com.creatorvault.api.model

import java.util.UUID

data class VideoResponse(
    val id: UUID,
    val title: String?,
    val sourceUrl: String,
    val status: String?,
    val fileUrl: String?
)
