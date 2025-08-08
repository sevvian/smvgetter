package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.newExtractorLink

class VidSrcTo : ExtractorApi("VidSrcTo", "https://vidsrc.to", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val sourceUrl = Regex("""sources: \[\{file:"(.*?)"\}\]""").find(response)?.groupValues?.get(1) ?: return
        val decodedUrl = "https://vidsrc.to${sourceUrl.substring(1)}"
        val sourceResponse = app.get(decodedUrl, referer = url).text
        val links = Regex("""(https.*m3u8)""").findAll(sourceResponse).map { it.value }.toList()
        links.forEach { link ->
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    link,
                    url,
                    -1,
                    isM3u8 = true
                )
            )
        }
    }
}