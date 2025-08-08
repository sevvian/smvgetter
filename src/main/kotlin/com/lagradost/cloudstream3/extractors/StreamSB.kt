package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

class StreamSB : ExtractorApi("StreamSB", "https://streamsb.net", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val masterUrl = Regex("""sources:\[\{file:"(.*?)"\}\]""").find(response)?.groupValues?.get(1)
        if (masterUrl != null) {
            val masterPlaylist = app.get(masterUrl).text
            val videoList = masterPlaylist.split("#EXT-X-STREAM-INF:").filter { it.contains("m3u8") }
            videoList.forEach {
                val quality = it.substringAfter("RESOLUTION=").substringAfter("x").substringBefore("\n")
                val link = it.substringAfter("\n").substringBefore("\n")
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
}