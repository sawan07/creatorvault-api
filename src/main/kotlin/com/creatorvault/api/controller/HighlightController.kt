package com.creatorvault.api.controller

import com.creatorvault.api.model.HighlightRequest
import com.creatorvault.api.model.HighlightResponse
import com.creatorvault.api.model.toResponse
import com.creatorvault.api.service.HighlightService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/highlights")
class HighlightController(
    private val highlightService: HighlightService
) {

    @GetMapping
    fun listHighlights(): List<HighlightResponse> =
        highlightService.getAllHighlights().map { it.toResponse() }

    @GetMapping("/video/{videoId}")
    fun getVideoHighlights(@PathVariable videoId: UUID): List<HighlightResponse> =
        highlightService.getHighlights(videoId).map { it.toResponse() }

    @GetMapping("/{id}")
    fun getHighlight(@PathVariable id: UUID): HighlightResponse =
        highlightService.getHighlightById(id)!!.toResponse()

    @PostMapping
    fun createHighlight(@RequestBody req: HighlightRequest): HighlightResponse =
        highlightService.createHighlight(req.videoId, req.startTime, req.endTime, req.label).toResponse()

    @PatchMapping("/{id}")
    fun updateHighlight(@PathVariable id: UUID, @RequestBody req: HighlightRequest): HighlightResponse =
        highlightService.updateHighlight(
            id = id,
            startTime = req.startTime,
            endTime = req.endTime,
            label = req.label
        ).toResponse()

    @DeleteMapping("/{id}")
    fun deleteHighlight(@PathVariable id: UUID) =
        highlightService.deleteHighlight(id)
}
