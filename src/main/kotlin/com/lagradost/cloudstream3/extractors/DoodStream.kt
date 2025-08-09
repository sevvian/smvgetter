package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import java.net.URI

class DoodStream : ExtractorApi("DoodStream", "https://dood.watch", requiresReferer = false) {
    override val altUrls = listOf("dood.to", "dood.so", "dood.la", "dood.pm", "dood.wf", "dood.ws", "dood.sh")

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val newUrl = if (url.contains("/d/")) url.replace("/d/", "/e/") else url
        val domain = URI(newUrl).host
        val response = app.get(newUrl, referer = referer).text
        val doodUrl = Regex("""/pass_md5/[^']+'""").find(response)?.value?.trim('\'')
        val key = doodUrl?.let { app.get("https://$domain$it", referer = newUrl).text } ?: return
        val quality = response.substringAfter("'/d/").substringBefore("',")
        val randomString = (0..9).joinToString("") { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random().toString() }
        val realUrl = "$key$randomString?token=${doodUrl.substringAfterLast("/")}&expiry=${System.currentTimeMillis()}"
        val headers = mapOf("Referer" to "https://$domain/")
        callback(
            newExtractorLink(
                this.name,
                this.name,
                realUrl,
                "https://$domain/",
                getQualityFromName(quality),
                headers = headers
            )
        )
    }
}