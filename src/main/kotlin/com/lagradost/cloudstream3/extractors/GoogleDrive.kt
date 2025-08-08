package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class GoogleDrive : ExtractorApi("GoogleDrive", "https://drive.google.com", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val links = Regex(""",\"(https.*?)\",(\d+),(\d+)""").findAll(response).toList()
        links.forEach {
            val link = it.groupValues[1].replace("\\u003d", "=").replace("\\u0026", "&")
            val quality = it.groupValues[3]
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