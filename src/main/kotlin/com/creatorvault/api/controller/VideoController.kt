package com.creatorvault.api.controller

import com.creatorvault.api.model.DownloadRequest
import com.creatorvault.api.model.JobStatusResponse
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

    // GET /videos?channelId=... or X-Google-Token
    @GetMapping
    fun list(
        @RequestHeader(name = "X-Google-Token", required = false) googleToken: String?,
        @RequestParam(name = "channelId", required = false) channelId: String?
    ): ResponseEntity<VideoListResponse> {
        val items = yt.listUploads(googleToken = googleToken, channelId = channelId)
        return ResponseEntity.ok(VideoListResponse(items))
    }

    // ✅ Updated: POST /videos/download  (matches Python service)
    @PostMapping("/download")
    fun startDownload(@RequestBody req: DownloadRequest): ResponseEntity<JobStatusResponse> {
        val response = videoSvc.startDownload(req)
        return ResponseEntity.ok(response)
    }

    // ✅ Same structure: GET /videos/status/{jobId}
    @GetMapping("/status/{jobId}")
    fun status(@PathVariable jobId: String): ResponseEntity<JobStatusResponse> {
        val response = videoSvc.getStatus(jobId)
        return ResponseEntity.ok(response)
    }
}
