package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Rabbitstream : ExtractorApi("Rabbitstream", "https://rabbitstream.net", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("embed-4/").substringBefore("?")
        val sourceUrl = "https://rabbitstream.net/ajax/embed-4/getSources?id=$id"
        val response = app.get(sourceUrl, referer = url, headers = mapOf("X-Requested-With" to "XMLHttpRequest")).parsed<RabbitstreamResponse>()
        response.sources.forEach {
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    it.file,
                    url,
                    -1,
                    isM3u8 = it.file.contains(".m3u8")
                )
            )
        }
    }

    @Serializable
    data class RabbitstreamResponse(
        @JsonProperty("sources") val sources: List<RabbitstreamSource>
    )

    @Serializable
    data class RabbitstreamSource(
        @JsonProperty("file") val file: String
    )
}