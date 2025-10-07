package com.creatorvault.api.service

import com.creatorvault.api.model.VideoItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Service
class YouTubeService(
    private val web: WebClient,
    @Value("\${youtube.apiKey:}") private val apiKey: String
) {
    private val base = "https://www.googleapis.com/youtube/v3"

    fun listUploads(googleToken: String?, channelId: String?, maxResults: Int = 25): List<VideoItem> {
        // 1) find uploads playlist id
        val uploadsId = if (!googleToken.isNullOrBlank()) {
            // OAuth path (mine=true)
            val ch = web.get()
                .uri("$base/channels?part=contentDetails&mine=true")
                .header("Authorization", "Bearer $googleToken")
                .retrieve().bodyToMono(Map::class.java).block()
            val items = ch?.get("items") as? List<*> ?: emptyList<Any>()
            val contentDetails = ((items.firstOrNull() as? Map<*, *>)?.get("contentDetails") as? Map<*, *>)
            val related = contentDetails?.get("relatedPlaylists") as? Map<*, *>
            related?.get("uploads") as? String ?: throw IllegalStateException("No uploads playlist found")
        } else {
            // API key path (requires channelId)
            require(!channelId.isNullOrBlank()) { "channelId is required when X-Google-Token is not provided" }
            val ch = web.get()
                .uri("$base/channels?part=contentDetails&id=$channelId&key=$apiKey")
                .retrieve().bodyToMono(Map::class.java).block()
            val items = ch?.get("items") as? List<*> ?: emptyList<Any>()
            val contentDetails = ((items.firstOrNull() as? Map<*, *>)?.get("contentDetails") as? Map<*, *>)
            val related = contentDetails?.get("relatedPlaylists") as? Map<*, *>
            related?.get("uploads") as? String ?: throw IllegalStateException("No uploads playlist found")
        }

        // 2) list playlist items -> videoIds + titles + thumbs
        val playlistUrl = StringBuilder("$base/playlistItems?part=contentDetails,snippet&playlistId=$uploadsId&maxResults=$maxResults")
        if (googleToken.isNullOrBlank()) playlistUrl.append("&key=$apiKey")

        val pl = web.get()
            .uri(playlistUrl.toString())
            .apply {
                if (!googleToken.isNullOrBlank()) header("Authorization", "Bearer $googleToken")
            }
            .retrieve().bodyToMono(Map::class.java).block()

        val items = pl?.get("items") as? List<Map<*, *>> ?: emptyList()
        val videoIds = items.mapNotNull { ((it["contentDetails"] as? Map<*, *>)?.get("videoId") as? String) }

        if (videoIds.isEmpty()) return emptyList()

        // 3) fetch durations in one call
        val idsCsv = videoIds.joinToString(",")
        val videosUrl = StringBuilder("$base/videos?part=contentDetails,snippet&id=$idsCsv")
        if (googleToken.isNullOrBlank()) videosUrl.append("&key=$apiKey")

        val vids = web.get()
            .uri(videosUrl.toString())
            .apply {
                if (!googleToken.isNullOrBlank()) header("Authorization", "Bearer $googleToken")
            }
            .retrieve().bodyToMono(Map::class.java).block()

        val vItems = vids?.get("items") as? List<Map<*, *>> ?: emptyList()

        // map videoId -> duration seconds
        val durationMap: Map<String, Long> = vItems.associate { vi ->
            val id = vi["id"] as String
            val c = vi["contentDetails"] as Map<*, *>
            val iso = c["duration"] as String // e.g. PT1H02M10S
            id to parseIsoDurationSeconds(iso)
        }

        // build VideoItem list
        val titleThumbMap: Map<String, Pair<String, String?>> = items.associate { pi ->
            val cd = pi["contentDetails"] as Map<*, *>
            val sn = pi["snippet"] as Map<*, *>
            val id = cd["videoId"] as String
            val title = sn["title"] as? String ?: "(untitled)"
            val thumbs = (sn["thumbnails"] as? Map<*, *>)?.get("medium") as? Map<*, *>
            val thumbUrl = thumbs?.get("url") as? String
            id to (title to thumbUrl)
        }

        return videoIds.map { id ->
            val (title, thumb) = titleThumbMap[id] ?: (id to null)
            VideoItem(
                youtubeId = id,
                title = title,
                durationSeconds = durationMap[id] ?: 0L,
                thumbnailUrl = thumb
            )
        }
    }

    private fun parseIsoDurationSeconds(iso: String): Long {
        // java.time.Duration parses ISO-8601 (e.g. PT1H2M10S)
        return Duration.parse(iso).seconds
    }
}
