package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class Archive : ExtractorApi("Archive", "https://archive.org", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val links = Regex("""<source src="(.*?)" type="(.*?)"(.*?)>""").findAll(response).toList()
        links.forEach {
            val link = it.groupValues[1]
            val quality = Regex("""res="(.*?)"""").find(it.groupValues[3])?.groupValues?.get(1)
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