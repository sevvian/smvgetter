package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.Qualities
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.newExtractorLink

class StreamSB : ExtractorApi("StreamSB", "https://streamsb.net", requiresReferer = true) {
    override val altUrls = listOf("sbembed.com", "sbembed1.com", "sbplay.org", "sbvideo.net", "sbfull.com", "sbbrisk.com", "sbspeed.com", "watchsb.com")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
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