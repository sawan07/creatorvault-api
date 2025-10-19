package com.creatorvault.api.repository

import com.creatorvault.api.domain.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface VideoRepository : JpaRepository<Video, UUID> {
    fun findAllByUserId(userId: UUID): List<Video>
}