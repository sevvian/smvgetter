package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Voe : ExtractorApi("Voe", "https://voe.sx", requiresReferer = true) {
    override val altUrls = listOf("voe.ninja")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val hls = Regex("""'hls': '(.*)'""").find(response)?.groupValues?.get(1)
        val quality = Regex("""'video_height': (\d+),""").find(response)?.groupValues?.get(1)
        if (hls != null) {
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    hls,
                    url,
                    getQualityFromName(quality),
                    isM3u8 = true
                )
            )
        }
    }
}