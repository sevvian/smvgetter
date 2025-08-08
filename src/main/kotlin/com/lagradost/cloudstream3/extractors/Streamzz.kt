package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Streamzz : ExtractorApi("Streamzz", "https://streamzz.to", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/")
        val response = app.get("https://streamzz.to/api/stream/info?id=$id", referer = url).parsed<StreamzzResponse>()
        response.data?.files?.forEach { file ->
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    file.src,
                    url,
                    getQualityFromName(file.label),
                )
            )
        }
    }

    @Serializable
    data class StreamzzResponse(
        @JsonProperty("data") val data: VideoData? = null
    )

    @Serializable
    data class VideoData(
        @JsonProperty("files") val files: List<VideoFile>? = null
    )

    @Serializable
    data class VideoFile(
        @JsonProperty("src") val src: String,
        @JsonProperty("label") val label: String
    )
}