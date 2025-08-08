package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Aparat : ExtractorApi("Aparat", "https://www.aparat.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("/v/")
        val response = app.get("https://www.aparat.com/api/fa/v1/video/video/show/videohash/$id").parsed<AparatResponse>()
        response.data?.attributes?.fileLink?.forEach {
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    it.url,
                    url,
                    getQualityFromName(it.quality),
                    it.url.contains(".m3u8")
                )
            )
        }
    }

    @Serializable
    data class AparatResponse(
        @JsonProperty("data") val data: Data? = null
    )

    @Serializable
    data class Data(
        @JsonProperty("attributes") val attributes: Attributes? = null
    )

    @Serializable
    data class Attributes(
        @JsonProperty("file_link_all") val fileLink: List<FileLink>? = null
    )

    @Serializable
    data class FileLink(
        @JsonProperty("url") val url: String,
        @JsonProperty("quality") val quality: String
    )
}