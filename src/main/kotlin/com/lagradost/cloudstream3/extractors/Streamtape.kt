package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import java.net.URI

class Streamtape : ExtractorApi("Streamtape", "https://streamtape.com", requiresReferer = true) {
    override val altUrls = listOf("streamtape.net", "streamtape.to", "streamtape.cc", "streamtape.video")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val link = Regex("""document\.getElementById\('botlink'\)\.innerHTML = '(.+)'""").find(response)?.groupValues?.get(1)?.substringAfter("?token=")
        val quality = Regex(""">(\d{3,4}p)</span>""").find(response)?.groupValues?.get(1)
        if (link != null) {
            val domain = URI(url).host
            val id = url.substringAfter("/e/")
            val realUrl = "https://$domain/get_video?id=$id&expires=${link.substringBefore("&")}&ip=${link.substringAfter("ip=").substringBefore("&")}&token=${link.substringAfter("token=")}"
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