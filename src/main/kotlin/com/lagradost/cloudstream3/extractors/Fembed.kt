package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Fembed : ExtractorApi("Fembed", "https://www.fembed.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("/v/")
        val response = app.post("https://www.fembed.com/api/source/$id", referer = url).parsed<FembedResponse>()
        if (response.success) {
            response.data?.forEach {
                callback(
                    newExtractorLink(
                        this.name,
                        it.label ?: this.name,
                        it.file,
                        url,
                        getQualityFromName(it.label),
                        it.type == "hls"
                    )
                )
            }
        }
    }

    @Serializable
    data class FembedResponse(
        @JsonProperty("success") val success: Boolean,
        @JsonProperty("data") val data: List<FembedData>? = null
    )

    @Serializable
    data class FembedData(
        @JsonProperty("file") val file: String,
        @JsonProperty("label") val label: String? = null,
        @JsonProperty("type") val type: String
    )
}