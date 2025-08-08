package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName

class StreamTape : ExtractorApi("StreamTape", "https://streamtape.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val link = Regex(""""botlink" style="display:none;">(.*)""").find(response)?.groupValues?.get(1)
        val quality = Regex(""">(\d{3,4}p)</span>""").find(response)?.groupValues?.get(1)
        if (link != null) {
            val realUrl = "https:${link}"
            callback(
                ExtractorLink(
                    this.name,
                    this.name,
                    realUrl,
                    url,
                    getQualityFromName(quality),
                )
            )
        }
    }
}