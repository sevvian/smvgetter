package com.extractor.api

import android.util.Log
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.ExtractorApi

object ExtractorLogic {
    private const val TAG = "ExtractorLogic"

    // A comprehensive list of all viable, ported extractors.
    private val extractors: List<ExtractorApi> = listOf(
        // Standard hosts
        DoodStream(),
        Streamtape(),
        Streamwish(),
        MixDrop(),
        Filemoon(),
        Voe(),
        Vidplay(),
        MyCloud(),
        StreamLare(),
        Vidoza(),
        Mp4upload(),
        Streamzz(),
        Aparat(),
        Archive(),
        Clipwatching(),
        DailyMotion(),
        Evoload(),
        Fembed(),
        FileLions(),
        Gofile(),
        GoogleDrive(),
        Highstream(),
        Mediafire(),
        Megacloud(),
        OkRu(),
        Streamhub(),
        Uqload(),
        Vidmoly(),
        Vimeo(),
        Yourupload(),
        StreamSB(),
        Netu(),
        Sendvid(),
        WcoStream()
        // Clones and variants are often covered by the main extractors.
        // Excluded: YouTube (requires external library), WebView-based extractors.
    )

    suspend fun extract(url: String): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()
        val subtitles = mutableListOf<SubtitleFile>()

        // Find the first extractor that matches the input URL from our list.
        val extractor = extractors.find { url.contains(it.mainUrl, ignoreCase = true) }
            ?: throw Exception("No suitable extractor found for URL: $url")

        Log.d(TAG, "Using extractor: ${extractor.name} for URL: $url")

        // Run the extractor
        extractor.getUrl(
            url,
            null,
            { subtitle -> subtitles.add(subtitle) },
            { link -> links.add(link) }
        )

        return links
    }
}