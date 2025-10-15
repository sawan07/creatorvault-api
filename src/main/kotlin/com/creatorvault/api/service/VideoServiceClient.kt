package com.creatorvault.api.service

import com.creatorvault.api.model.DownloadRequest
import com.creatorvault.api.model.JobStatusResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class VideoServiceClient(
    private val webClient: WebClient,
    @Value("\${services.video.baseUrl:http://localhost:8001}") private val baseUrl: String,
    @Value("\${security.downstreamKey:downstream-key}") private val downstreamKey: String
) {
    fun startDownload(req: DownloadRequest): JobStatusResponse {
        return webClient.post()
            .uri("$baseUrl/videos/download")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $downstreamKey")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(JobStatusResponse::class.java)
            .block()!!
    }

    fun getStatus(jobId: String): JobStatusResponse {
        return webClient.get()
            .uri("$baseUrl/videos/status/$jobId")
            .header("Authorization", "Bearer $downstreamKey")
            .retrieve()
            .bodyToMono(JobStatusResponse::class.java)
            .block()!!
    }
}
