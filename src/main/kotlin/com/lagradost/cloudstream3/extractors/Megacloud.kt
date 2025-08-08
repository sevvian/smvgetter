package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.Qualities
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Megacloud : ExtractorApi(
    "Megacloud",
    "https://megacloud.tv",
    requiresReferer = true
) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfter("/embed-2/").substringBefore("?")
        val response = app.get("https://megacloud.tv/embed-2/ajax/e-1/getSources?id=$id", referer = url).parsed<MegacloudResponse>()
        if (response.encrypted) {
            // This is a simplified placeholder for a complex decryption process
            // A full implementation would require porting the decryption logic from JS
        } else {
            response.sources?.forEach {
                callback(
                    newExtractorLink(
                        this.name,
                        this.name,
                        it.file,
                        url,
                        Qualities.Unknown,
                        isM3u8 = it.file.contains(".m3u8")
                    )
                )
            }
        }
    }

    @Serializable
    data class MegacloudResponse(
        @JsonProperty("encrypted") val encrypted: Boolean,
        @JsonProperty("sources") val sources: List<MegacloudSource>? = null
    )

    @Serializable
    data class MegacloudSource(
        @JsonProperty("file") val file: String,
        @JsonProperty("type") val type: String
    )
}