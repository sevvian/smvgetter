package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class StreamLare : ExtractorApi("StreamLare", "https://streamlare.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/")
        val response = app.post("https://streamlare.com/api/v1/video/stream/get", data = mapOf("id" to id), referer = url).parsed<StreamLareResponse>()
        if (response.status == "success") {
            response.result?.forEach { (_, value) ->
                callback(
                    newExtractorLink(
                        this.name,
                        value.label ?: this.name,
                        value.file,
                        url,
                        getQualityFromName(value.label),
                        value.type == "hls"
                    )
                )
            }
        }
    }

    @Serializable
    data class StreamLareResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("result") val result: Map<String, StreamLareResult>? = null
    )

    @Serializable
    data class StreamLareResult(
        @JsonProperty("label") val label: String? = null,
        @JsonProperty("file") val file: String,
        @JsonProperty("type") val type: String
    )
}