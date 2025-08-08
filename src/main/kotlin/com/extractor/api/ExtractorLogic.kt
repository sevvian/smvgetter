package com.extractor.api

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
// Manually import the extractors we want to support
import com.lagradost.cloudstream3.extractors.DoodStream
import com.lagradost.cloudstream3.extractors.StreamTape
import com.lagradost.cloudstream3.extractors.Streamwish

object ExtractorLogic {
    private const val TAG = "ExtractorLogic"

    // Instead of reflection, we use a manually curated list of extractors.
    // This is more robust and avoids classpath scanning issues.
    private val extractors: List<ExtractorApi> = listOf(
        DoodStream(),
        StreamTape(),
        Streamwish()
        // We can easily add more extractors here in the future
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