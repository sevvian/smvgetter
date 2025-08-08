package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Yourupload : ExtractorApi("Yourupload", "https://www.yourupload.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val link = Regex("""file:\s*'(.*?)'""").find(response)?.groupValues?.get(1)
        if (link != null) {
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    link,
                    url,
                    getQualityFromName(""),
                )
            )
        }
    }
}