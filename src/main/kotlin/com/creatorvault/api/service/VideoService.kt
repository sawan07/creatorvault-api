package com.creatorvault.api.service

import com.creatorvault.api.domain.entity.*
import com.creatorvault.api.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class VideoService(
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createVideo(userId: UUID, sourceUrl: String, title: String? = null): Video {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val video = Video(user = user, sourceUrl = sourceUrl, title = title)
        return videoRepository.save(video)
    }

    fun getVideosByUser(userId: UUID): List<Video> = videoRepository.findAllByUserId(userId)
}