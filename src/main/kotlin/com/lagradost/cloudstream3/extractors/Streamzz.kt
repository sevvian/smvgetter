package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Streamzz : ExtractorApi("Streamzz", "https://streamzz.to", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/")
        val response = app.get("https://streamzz.to/video-info/$id", referer = url).parsed<StreamzzResponse>()
        response.video?.files?.forEach { (quality, file) ->
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    file.url,
                    url,
                    getQualityFromName(quality),
                )
            )
        }
    }

    @Serializable
    data class StreamzzResponse(
        @JsonProperty("video") val video: Video? = null
    )

    @Serializable
    data class Video(
        @JsonProperty("files") val files: Map<String, File>? = null
    )

    @Serializable
    data class File(
        @JsonProperty("url") val url: String
    )
}