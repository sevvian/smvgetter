package com.extractor.api

import android.util.Log
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.ExtractorApi

object ExtractorLogic {
    private const val TAG = "ExtractorLogic"

    // By creating a comprehensive list, we can support a wide range of sources.
    // Adding a new extractor is as simple as adding its class to this list.
    private val extractors: List<ExtractorApi> = listOf(
        DoodStream(),
        Streamtape(),
        Streamwish(),
        MixDrop(),
        Filemoon(),
        Voe()
        // More can be added here as they are ported.
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