package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Netu : ExtractorApi("Netu", "https://netu.tv", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val videoUrl = Regex("""'hls', '(.*?)'""").find(response)?.groupValues?.get(1)
        if (videoUrl != null) {
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    videoUrl,
                    url,
                    getQualityFromName(""),
                    isM3u8 = true
                )
            )
        }
    }
}