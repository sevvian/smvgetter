package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.newExtractorLink

class Sibnet : ExtractorApi("Sibnet", "https://video.sibnet.ru", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val videoUrl = Regex("""player\.src\(\[\{src: "(.*?)"""").find(response)?.groupValues?.get(1)
        if (videoUrl != null) {
            val finalUrl = "https://video.sibnet.ru$videoUrl"
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    finalUrl,
                    url,
                    -1
                )
            )
        }
    }
}