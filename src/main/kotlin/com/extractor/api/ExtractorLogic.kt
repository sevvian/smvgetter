package com.extractor.api

import android.util.Log
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.extractors.DoodStream
import com.lagradost.cloudstream3.extractors.Streamtape
import com.lagradost.cloudstream3.extractors.Streamwish
import com.lagradost.cloudstream3.utils.ExtractorApi

object ExtractorLogic {
    private const val TAG = "ExtractorLogic"

    private val extractors: List<ExtractorApi> = listOf(
        DoodStream(),
        Streamtape(),
        Streamwish()
    )

    suspend fun extract(url: String): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()
        val subtitles = mutableListOf<SubtitleFile>()

        val extractor = extractors.find { url.contains(it.mainUrl, ignoreCase = true) }
            ?: throw Exception("No suitable extractor found for URL: $url")

        Log.d(TAG, "Using extractor: ${extractor.name} for URL: $url")

        extractor.getUrl(
            url,
            null,
            { subtitle -> subtitles.add(subtitle) },
            { link -> links.add(link) }
        )

        return links
    }
}