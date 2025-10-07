package com.creatorvault.api.controller

import com.creatorvault.api.model.VideoListResponse
import com.creatorvault.api.service.VideoServiceClient
import com.creatorvault.api.service.YouTubeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/videos")
class VideoController(
    private val videoSvc: VideoServiceClient,
    private val yt: YouTubeService
) {

    // NEW: real YouTube fetch.
    // Pass Google OAuth token in X-Google-Token (preferred),
    // OR pass ?channelId=UCxxxx and set YOUTUBE_API_KEY env var.
    @GetMapping
    fun list(
        @RequestHeader(name = "X-Google-Token", required = false) googleToken: String?,
        @RequestParam(name = "channelId", required = false) channelId: String?
    ): ResponseEntity<VideoListResponse> {
        val items = yt.listUploads(googleToken = googleToken, channelId = channelId)
        return ResponseEntity.ok(VideoListResponse(items))
    }

    // (unchanged) start download
    @PostMapping("/{youtubeId}/download")
    fun startDownload(@PathVariable youtubeId: String): ResponseEntity<Any> =
        ResponseEntity.ok(videoSvc.startDownload(com.creatorvault.api.model.DownloadRequest(youtubeId)).block())

    // (unchanged) status
    @GetMapping("/{jobId}/status")
    fun status(@PathVariable jobId: String) =
        ResponseEntity.ok(videoSvc.getStatus(jobId).block())
}
