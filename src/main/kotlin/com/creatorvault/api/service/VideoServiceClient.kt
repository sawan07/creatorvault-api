package com.creatorvault.api.service


import com.creatorvault.api.model.DownloadRequest
import com.creatorvault.api.model.JobStatusResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class VideoServiceClient(
    private val webClient: WebClient,
    @Value("\${services.video.baseUrl:http://localhost:8001}") private val baseUrl: String,
    @Value("\${security.downstreamKey:downstream-key}") private val downstreamKey: String
) {
    fun startDownload(req: DownloadRequest) =
        webClient.post()
            .uri("$baseUrl/download")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $downstreamKey")
            .bodyValue(req)
            .retrieve()
            .bodyToMono(Map::class.java)


    fun getStatus(jobId: String) =
        webClient.get()
            .uri("$baseUrl/status/$jobId")
            .header("Authorization", "Bearer $downstreamKey")
            .retrieve()
            .bodyToMono(JobStatusResponse::class.java)
}