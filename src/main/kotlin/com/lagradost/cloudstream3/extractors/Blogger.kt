package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Blogger : ExtractorApi("Blogger", "https://blogspot.com", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val links = Regex("""\{"label":"(\d+p)","type":"video/mp4","file":"(.*?)"\}""").findAll(response).toList()
        links.forEach {
            val quality = it.groupValues[1]
            val link = it.groupValues[2]
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    link,
                    url,
                    getQualityFromName(quality)
                )
            )
        }
    }
}