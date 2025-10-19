package com.creatorvault.api.service

import com.creatorvault.api.domain.entity.*
import com.creatorvault.api.repository.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class VideoPersistenceService(
    private val userRepo: UserRepository,
    private val videoRepo: VideoRepository
) {

    fun saveVideoMetadata(
        userEmail: String,
        title: String?,
        sourceUrl: String,
        fileUrl: String?,
        status: String
    ): Video {
        val user = userRepo.findByEmail(userEmail)
            ?: userRepo.save(User(email = userEmail))

        val video = Video(
            title = title,
            sourceUrl = sourceUrl,
            fileUrl = fileUrl,
            status = status,
            user = user
        )

        return videoRepo.save(video)
    }

    fun listVideosByUser(email: String): List<Video> {
        val user = userRepo.findByEmail(email)
            ?: throw IllegalArgumentException("User not found: $email")
        return videoRepo.findAllByUserId(user.id!!) // Using !! since we know user exists
    }
}
