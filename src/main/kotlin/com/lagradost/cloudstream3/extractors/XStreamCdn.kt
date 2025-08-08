package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class XStreamCdn : ExtractorApi("XStreamCdn", "https://xstreamcdn.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("v/").substringBefore("?")
        val apiUrl = "https://xstreamcdn.com/api/source/$id"
        val response = app.post(apiUrl, referer = url).parsed<XStreamCdnResponse>()
        if (response.success) {
            response.data?.forEach {
                callback(
                    newExtractorLink(
                        this.name,
                        it.label,
                        it.file,
                        url,
                        -1,
                        isM3u8 = it.type == "hls"
                    )
                )
            }
        }
    }

    @Serializable
    data class XStreamCdnResponse(
        @JsonProperty("success") val success: Boolean,
        @JsonProperty("data") val data: List<XStreamCdnData>? = null
    )

    @Serializable
    data class XStreamCdnData(
        @JsonProperty("file") val file: String,
        @JsonProperty("label") val label: String,
        @JsonProperty("type") val type: String
    )
}