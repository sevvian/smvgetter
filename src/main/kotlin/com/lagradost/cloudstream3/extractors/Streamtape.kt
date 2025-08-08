package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Streamtape : ExtractorApi("Streamtape", "https://streamtape.com", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val link = Regex("""document\.getElementById\('botlink'\)\.innerHTML = '(.+)'""").find(response)?.groupValues?.get(1)?.substringAfter("?token=")
        val quality = Regex(""">(\d{3,4}p)</span>""").find(response)?.groupValues?.get(1)
        if (link != null) {
            val realUrl = "https://streamtape.com/get_video?id=${url.substringAfter("/e/")}&expires=${link.substringBefore("&")}&ip=${link.substringAfter("ip=").substringBefore("&")}&token=${link.substringAfter("token=")}"
            callback(
                newExtractorLink(
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