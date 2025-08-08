package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.JsUnpacker
import com.lagradost.cloudstream3.utils.newExtractorLink

class Filemoon : ExtractorApi("Filemoon", "https://filemoon.sx", requiresReferer = true) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val packed = Regex("""eval\((.*)\)""").find(response)?.groupValues?.get(1)
        if (packed != null) {
            val unpacked = JsUnpacker(packed).unpack()
            val link = Regex("""file:"(.*?)"""").find(unpacked ?: "")?.groupValues?.get(1)
            if (link != null) {
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
}