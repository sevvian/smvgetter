package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.JsUnpacker
import com.lagradost.cloudstream3.utils.newExtractorLink

class Filemoon : ExtractorApi("Filemoon", "https://filemoon.sx", requiresReferer = true) {
    override val altUrls = listOf("filemoon.to", "filemoon.ws", "filemoon.in", "filemoon.so")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val packed = Regex("""eval\(function\(p,a,c,k,e,d\)(.*)\)""").find(response)?.groupValues?.get(1)
        if (packed != null) {
            val unpacked = JsUnpacker("function(p,a,c,k,e,d)$packed").unpack()
            val link = Regex("""file:\s*"(https://[^"]+master\.m3u8[^"]*)"""").find(unpacked ?: "")?.groupValues?.get(1)
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