package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.*

class Streamwish : ExtractorApi("Streamwish", "https://streamwish.to", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val packed = Regex("""eval\(function\(p,a,c,k,e,d\)(.*)\)""").find(response)?.groupValues?.get(1)
        if (packed != null) {
            val decoded = JsUnpacker(packed).unpack()
            val links = Regex("""file:"(.*)"""").findAll(decoded.toString()).map { it.groupValues[1] }.toList()
            links.forEach { link ->
                callback(
                    newExtractorLink(
                        this.name,
                        this.name,
                        link,
                        url,
                        getQualityFromName(""),
                        link.contains(".m3u8")
                    )
                )
            }
        }
    }
}