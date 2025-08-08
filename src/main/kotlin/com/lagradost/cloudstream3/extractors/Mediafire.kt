package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Mediafire : ExtractorApi("Mediafire", "https://www.mediafire.com", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val link = Regex("""href="(https://download\d+\.mediafire\.com/.*?)"""").find(response)?.groupValues?.get(1)
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