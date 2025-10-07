package com.creatorvault.api.model
import jakarta.validation.constraints.NotBlank
// Auth
data class MeResponse(
    val email: String,
    val channelId: String? = null
)
// Videos
data class VideoItem(
    val youtubeId: String,
    val title: String,
    val durationSeconds: Long,
    val thumbnailUrl: String?
)
data class VideoListResponse(val items: List<VideoItem>)
data class DownloadRequest(@field:NotBlank val youtubeId: String)
data class JobStatusResponse(
    val jobId: String,
    val status: String, // QUEUED, RUNNING, DONE, ERROR
    val fileUrl: String? = null,
    val message: String? = null
)
// Highlights
data class AnalyzeRequest(@field:NotBlank val videoId: String, val fileUrl: String)
data class Highlight(
    val startTime: Double,
    val endTime: Double,
    val confidence: Double
)
data class HighlightsResponse(val videoId: String, val items: List<Highlight>)