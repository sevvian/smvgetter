package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class OkRu : ExtractorApi("OkRu", "https://ok.ru", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val json = Regex("""data-options='(.*?)'""").find(response)?.groupValues?.get(1)?.replace("&quot;", "\"")
        val links = Regex(""""url":"(.*?)","name":"(.*?)"""").findAll(json ?: "").toList()
        links.forEach {
            val link = it.groupValues[1].replace("\\u0026", "&")
            val quality = it.groupValues[2]
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