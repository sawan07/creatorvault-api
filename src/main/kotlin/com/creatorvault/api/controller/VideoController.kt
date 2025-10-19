package com.creatorvault.api.controller

import com.creatorvault.api.domain.entity.Video
import com.creatorvault.api.model.VideoRequest
import com.creatorvault.api.model.VideoResponse
import com.creatorvault.api.service.VideoService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/videos")
class VideoController(
    private val videoService: VideoService
) {

    @GetMapping("/user/{userId}")
    fun getUserVideos(@PathVariable userId: UUID): List<Video> =
        videoService.getVideosByUser(userId)

    @PostMapping
    fun createVideo(@RequestBody req: VideoRequest): VideoResponse {
        val video = videoService.createVideo(req.userId, req.sourceUrl, req.title)
        return VideoResponse(
            id = video.id!!,
            title = video.title,
            sourceUrl = video.sourceUrl,
            status = video.status,
            fileUrl = video.fileUrl
        )
    }

}
