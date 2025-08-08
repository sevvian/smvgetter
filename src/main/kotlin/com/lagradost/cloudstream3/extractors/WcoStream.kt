package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class WcoStream : ExtractorApi("WcoStream", "https://wcostream.co", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("embed/").substringBefore("?")
        val response = app.get("https://wcostream.co/api/v1/episode/info?id=$id", referer = url).parsed<WcoResponse>()
        response.source?.forEach {
            callback(
                newExtractorLink(
                    this.name,
                    it.label ?: this.name,
                    it.src,
                    url,
                    getQualityFromName(it.label),
                    it.src.contains(".m3u8")
                )
            )
        }
    }

    @Serializable
    data class WcoResponse(
        @JsonProperty("source") val source: List<WcoSource>? = null
    )

    @Serializable
    data class WcoSource(
        @JsonProperty("src") val src: String,
        @JsonProperty("label") val label: String? = null
    )
}