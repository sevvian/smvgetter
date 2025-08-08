package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink

class DoodStream : ExtractorApi("DoodStream", "https://dood.watch", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val newUrl = if (url.contains("/d/")) url.replace("/d/", "/e/") else url
        val response = app.get(newUrl, referer = referer).text
        val doodUrl = Regex("""\$\.get\('(/pass_md5/.*?)'""").find(response)?.groupValues?.get(1)
        val key = doodUrl?.let { app.get("https://dood.watch$it", referer = newUrl).text }
        val quality = response.substringAfter("'/d/").substringBefore("',")
        val realUrl = "$key${
            (0..9).joinToString("") {
                (('a'..'z') + ('A'..'Z') + ('0'..'9')).random().toString()
            }
        }?token=${doodUrl?.substringAfterLast("/")}"
        val headers = mapOf("Referer" to "https://dood.watch/")
        callback(
            newExtractorLink(
                this.name,
                this.name,
                realUrl,
                "https://dood.watch/",
                getQualityFromName(quality),
                headers = headers
            )
        )
    }
}