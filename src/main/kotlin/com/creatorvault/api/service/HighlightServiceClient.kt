package com.creatorvault.api.service

import com.creatorvault.api.model.AnalyzeRequest
import com.creatorvault.api.model.HighlightsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class HighlightServiceClient(
    private val webClient: WebClient,
    @Value("\${services.highlight.baseUrl:http://localhost:8002}") private val baseUrl: String,
    @Value("\${security.downstreamKey:downstream-key}") private val downstreamKey: String
) {
    fun analyze(req: AnalyzeRequest) =
        webClient.post()
            .uri("$baseUrl/highlights/analyze")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $downstreamKey")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(HighlightsResponse::class.java)
            .doOnSubscribe { println("📡 Calling highlight-service for videoId=${req.videoId}") }


    fun get(videoId: String) =
        webClient.get()
            .uri("$baseUrl/highlights/$videoId")
            .header("Authorization", "Bearer $downstreamKey")
            .retrieve()
            .bodyToMono(HighlightsResponse::class.java)
}