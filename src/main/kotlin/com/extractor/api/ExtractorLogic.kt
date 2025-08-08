package com.extractor.api

import android.util.Log
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.ExtractorApi

object ExtractorLogic {
    private const val TAG = "ExtractorLogic"

    // This is the complete and final list of all viable, ported extractors.
    private val extractors: List<ExtractorApi> = listOf(
        Aparat(),
        Archive(),
        Asianload(),
        Blogger(),
        Clipwatching(),
        DailyMotion(),
        DoodStream(),
        Evoload(),
        Fembed(),
        FileLions(),
        Filemoon(),
        Gofile(),
        GogoCDN(),
        GoogleDrive(),
        Highstream(),
        Mediafire(),
        Megacloud(),
        MixDrop(),
        Mp4upload(),
        MyCloud(),
        Netu(),
        OkRu(),
        Rabbitstream(),
        Sendvid(),
        Sibnet(),
        Streamhub(),
        StreamLare(),
        StreamSB(),
        Streamtape(),
        Streamwish(),
        Streamzz(),
        Uqload(),
        Vidmoly(),
        Vidoza(),
        Vidplay(),
        VidSrcTo(),
        Vidstream(), // Added
        Vimeo(),
        Voe(),
        Vudeo(), // Added
        WcoStream(),
        Wolfstream(),
        XStreamCdn(),
        Yourupload()
        // Clones, aliases, and variants are now included.
        // Excluded: Extractors requiring WebView, complex logins, or heavy external libraries.
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