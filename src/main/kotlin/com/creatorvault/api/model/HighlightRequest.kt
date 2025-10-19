package com.creatorvault.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import java.util.*

data class HighlightRequest(
    @field:NotNull
    @JsonProperty("videoId")
    val videoId: UUID,

    @field:NotNull
    @JsonProperty("startTime")
    val startTime: Double,

    @field:NotNull
    @JsonProperty("endTime")
    val endTime: Double,

    @JsonProperty("label")
    val label: String? = null
)
