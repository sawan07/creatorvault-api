package com.creatorvault.api.controller


import com.creatorvault.api.model.AnalyzeRequest
import com.creatorvault.api.model.HighlightsResponse
import com.creatorvault.api.service.HighlightServiceClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/highlights")
class HighlightController(
    private val hiSvc: HighlightServiceClient
) {
    @PostMapping("/analyze")
    fun analyze(@RequestBody req: AnalyzeRequest): ResponseEntity<HighlightsResponse> =
        ResponseEntity.ok(hiSvc.analyze(req).block())


    @GetMapping("/{videoId}")
    fun get(@PathVariable videoId: String): ResponseEntity<HighlightsResponse> =
        ResponseEntity.ok(hiSvc.get(videoId).block())
}