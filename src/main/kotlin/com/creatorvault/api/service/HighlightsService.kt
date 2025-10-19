package com.creatorvault.api.service

import com.creatorvault.api.domain.entity.Highlight
import com.creatorvault.api.repository.HighlightRepository
import com.creatorvault.api.repository.VideoRepository
import com.creatorvault.api.exception.InvalidRequestException
import com.creatorvault.api.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class HighlightService(
    private val highlightRepository: HighlightRepository,
    private val videoRepository: VideoRepository
) {

    fun getAllHighlights(): List<Highlight> =
        highlightRepository.findAll()

    fun getHighlights(videoId: UUID): List<Highlight> {
        val video = videoRepository.findById(videoId)
            .orElseThrow { ResourceNotFoundException("Video not found for id: $videoId") }

        return highlightRepository.findAllByVideoId(video.id!!)
    }

    fun getHighlightById(id: UUID): Highlight? =
        highlightRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Highlight not found for id: $id")
        }

    @Transactional
    fun createHighlight(videoId: UUID, startTime: Double, endTime: Double, label: String?): Highlight {
        val video = videoRepository.findById(videoId)
            .orElseThrow { ResourceNotFoundException("Video not found for id: $videoId") }

        if (endTime <= startTime) {
            throw InvalidRequestException("endTime must be greater than startTime")
        }

        val highlight = Highlight(
            video = video,
            startTime = startTime,
            endTime = endTime,
            label = label
        )
        return highlightRepository.save(highlight)
    }

    @Transactional
    fun updateHighlight(id: UUID, startTime: Double?, endTime: Double?, label: String?): Highlight {
        val existing = highlightRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Highlight not found for id: $id") }

        if (startTime != null && endTime != null && endTime <= startTime) {
            throw InvalidRequestException("endTime must be greater than startTime")
        }

        val updated = existing.copy(
            startTime = startTime ?: existing.startTime,
            endTime = endTime ?: existing.endTime,
            label = label ?: existing.label
        )

        return highlightRepository.save(updated)
    }

    @Transactional
    fun deleteHighlight(id: UUID) {
        val existing = highlightRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Highlight not found for id: $id") }

        highlightRepository.delete(existing)
    }
}
