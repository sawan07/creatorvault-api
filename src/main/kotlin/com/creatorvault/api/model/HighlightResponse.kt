package com.creatorvault.api.model

import java.util.*

data class HighlightResponse(
    val id: UUID,
    val startTime: Double,
    val endTime: Double,
    val label: String?,
    val videoId: UUID
)
