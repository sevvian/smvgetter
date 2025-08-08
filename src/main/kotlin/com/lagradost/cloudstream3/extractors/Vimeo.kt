package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Vimeo : ExtractorApi("Vimeo", "https://vimeo.com", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/")
        val response = app.get("https://player.vimeo.com/video/$id/config").parsed<VimeoResponse>()
        response.request?.files?.progressive?.forEach {
            callback(
                newExtractorLink(
                    this.name,
                    it.quality ?: this.name,
                    it.url,
                    url,
                    getQualityFromName(it.quality),
                )
            )
        }
    }

    @Serializable
    data class VimeoResponse(
        @JsonProperty("request") val request: VimeoRequest? = null
    )

    @Serializable
    data class VimeoRequest(
        @JsonProperty("files") val files: VimeoFiles? = null
    )

    @Serializable
    data class VimeoFiles(
        @JsonProperty("progressive") val progressive: List<VimeoProgressive>? = null
    )

    @Serializable
    data class VimeoProgressive(
        @JsonProperty("quality") val quality: String? = null,
        @JsonProperty("url") val url: String
    )
}