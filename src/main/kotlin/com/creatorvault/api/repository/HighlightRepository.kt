package com.creatorvault.api.repository

import com.creatorvault.api.domain.entity.Highlight
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HighlightRepository : JpaRepository<Highlight, UUID> {
    fun findAllByVideoId(videoId: UUID): List<Highlight>
}