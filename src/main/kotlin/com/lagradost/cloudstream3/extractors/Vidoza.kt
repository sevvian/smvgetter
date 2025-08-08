package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Vidoza : ExtractorApi("Vidoza", "https://vidoza.net", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val link = Regex("""sourcesCode:\s*\[\{src:\s*"(.*?)"""").find(response)?.groupValues?.get(1)
        val quality = Regex("""\s*(\d{3,4}p)\s*""").find(response)?.groupValues?.get(1)
        if (link != null) {
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    link,
                    url,
                    getQualityFromName(quality),
                )
            )
        }
    }
}