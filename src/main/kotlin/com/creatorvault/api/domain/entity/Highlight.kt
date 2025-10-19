package com.creatorvault.api.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "highlights")
data class Highlight(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    val video: Video,

    @Column(name = "start_time", nullable = false)
    val startTime: Double,

    @Column(name = "end_time", nullable = false)
    val endTime: Double,

    val label: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
