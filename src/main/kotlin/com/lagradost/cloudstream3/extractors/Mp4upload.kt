package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.JsUnpacker
import com.lagradost.cloudstream3.utils.newExtractorLink

class Mp4upload : ExtractorApi("Mp4upload", "https://www.mp4upload.com", requiresReferer = true) {
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
            val link = Regex("""player\.src\("(.*?)"""").find(unpacked ?: "")?.groupValues?.get(1)
            if (link != null) {
                callback(
                    newExtractorLink(
                        this.name,
                        this.name,
                        link,
                        url,
                        -1,
                        headers = mapOf("Referer" to url)
                    )
                )
            }
        }
    }
}