package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class DailyMotion : ExtractorApi("DailyMotion", "https://www.dailymotion.com", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val json = Regex("""__PLAYER_CONFIG__\s*=\s*(\{.*?\})""").find(response)?.groupValues?.get(1)
        val links = Regex(""""url":"(.*?)","type":"application\\/x-mpegURL"""").findAll(json ?: "").map { it.groupValues[1] }.toList()
        links.forEach { link ->
            val quality = Regex("""/(\d+x\d+)/""").find(link)?.groupValues?.get(1)?.split("x")?.get(1)
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    link.replace("\\/", "/"),
                    url,
                    getQualityFromName(quality),
                    isM3u8 = true
                )
            )
        }
    }
}