package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.*

class Streamhub : ExtractorApi("Streamhub", "https://streamhub.to", requiresReferer = true) {
    override val altUrls = listOf("streamhub.ink", "streamhub.cx")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val packed = Regex("""eval\((.*)\)""").find(response)?.groupValues?.get(1)
        if (packed != null) {
            val unpacked = JsUnpacker(packed).unpack()
            val link = Regex("""sources:\s*\[\{file:"(.*?)"""").find(unpacked ?: "")?.groupValues?.get(1)
            if (link != null) {
                callback(
                    newExtractorLink(
                        this.name,
                        this.name,
                        link,
                        url,
                        getQualityFromName(""),
                        isM3u8 = link.contains(".m3u8")
                    )
                )
            }
        }
    }
}