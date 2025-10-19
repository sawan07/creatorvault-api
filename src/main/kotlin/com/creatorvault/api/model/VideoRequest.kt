package com.creatorvault.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import java.util.*

data class VideoRequest(
    @field:NotBlank
    @JsonProperty("userId")
    val userId: UUID,

    @field:NotBlank
    @JsonProperty("sourceUrl")
    val sourceUrl: String,

    @JsonProperty("title")
    val title: String? = null
)
