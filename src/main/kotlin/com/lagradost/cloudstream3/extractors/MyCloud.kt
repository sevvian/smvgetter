package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.Qualities
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.CryptoAES
import kotlinx.serialization.Serializable

class MyCloud : ExtractorApi(
    "MyCloud",
    "https://mycloud.to",
    requiresReferer = true
) {
    private val key = "2574153857214863"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val futoken = app.get("https://mycloud.to/futoken", referer = referer).text
        val id = url.substringAfterLast("/")
        val encodedUrl = encodeId(id, key)
        val realUrl = "https://mycloud.to/mediainfo/$encodedUrl?${futoken.replace("\"", "")}"
        
        val response = app.get(realUrl, referer = url).parsed<MediaInfo>()
        
        response.result?.sources?.firstOrNull()?.file?.let { fileUrl ->
            val result = app.get(fileUrl, referer = url).text
            val master = "#EXT-M3U8\\n#EXT-X-VERSION:3\\n".toRegex().replace(result, "")
            val videoList = master.split("#EXT-X-STREAM-INF:").filter { it.contains("m3u8") }
            videoList.forEach {
                val quality = it.substringAfter("RESOLUTION=").substringAfter("x").substringBefore("\\n")
                val link = it.substringAfter("\\n").substringBefore("\\n")
                callback(
                    newExtractorLink(
                        this.name,
                        this.name,
                        link,
                        url,
                        quality.toIntOrNull() ?: Qualities.Unknown,
                        isM3u8 = true
                    )
                )
            }
        }
    }

    private fun encodeId(id: String, key: String): String {
        val encrypted = CryptoAES.encrypt(id, key, key)
        return encrypted.replace(Regex("[/+]"), "-_").replace(Regex("="), "")
    }

    @Serializable
    data class MediaInfo(
        @JsonProperty("result") val result: Result? = null
    )

    @Serializable
    data class Result(
        @JsonProperty("sources") val sources: List<Source>
    )

    @Serializable
    data class Source(
        @JsonProperty("file") val file: String
    )
}