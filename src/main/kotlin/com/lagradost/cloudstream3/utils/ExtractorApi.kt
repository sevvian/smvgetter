package com.lagradost.cloudstream3.utils

import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.Qualities
import com.lagradost.cloudstream3.SubtitleFile

fun getQualityFromName(qualityName: String?): Int {
    return when (qualityName?.replace("p", "")?.trim()) {
        "2160" -> Qualities.P2160
        "1440" -> Qualities.P1440
        "1080" -> Qualities.P1080
        "720" -> Qualities.P720
        "480" -> Qualities.P480
        "360" -> Qualities.P360
        else -> Qualities.Unknown
    }
}

fun newExtractorLink(
    source: String,
    name: String,
    url: String,
    referer: String,
    quality: Int,
    isM3u8: Boolean = false,
    headers: Map<String, String> = mapOf(),
): ExtractorLink {
    return ExtractorLink(source, name, url, referer, quality, isM3u8, headers)
}

abstract class ExtractorApi(
    open var name: String,
    open var mainUrl: String,
    val requiresReferer: Boolean = false,
) {
    // This new property will hold a list of alternative domains for an extractor.
    open val altUrls: List<String> = emptyList()

    abstract suspend fun getUrl(
        url: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    )
}