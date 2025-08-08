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

abstract class ExtractorApi(
    var name: String,
    open var mainUrl: String,
    val requiresReferer: Boolean = false,
) {
    /**
     * Used to get the url and any other data from the page
     * url is the link to the page which is to be extracted
     * referer is the link to the page you are coming from, some sites require this
     * subtitleCallback is a callback to add subtitles
     * callback is a callback to add the link to the list of links
     */
    abstract suspend fun getUrl(
        url: String,
        referer: String? = null,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    )
}