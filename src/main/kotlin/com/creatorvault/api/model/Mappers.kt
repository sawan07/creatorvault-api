package com.creatorvault.api.model

import com.creatorvault.api.domain.entity.Highlight
import java.util.*

fun Highlight.toResponse(): HighlightResponse =
    HighlightResponse(
        id = this.id!!,
        startTime = this.startTime,
        endTime = this.endTime,
        label = this.label,
        videoId = this.video.id ?: UUID(0, 0)
    )
