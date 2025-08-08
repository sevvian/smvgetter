package com.extractor.api

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.extractors.*
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink

object ExtractorLogic {
    // A list of all supported extractor instances.
    // This list must be manually updated when new extractors are added to Cloudstream.
    private val extractors: List<ExtractorApi> = listOf(
        DoodLaExtractor(),
        StreamTape(),
        StreamWishExtractor(),
        FileMoon(),
        VidhideExtractor(),
        // Add other extractors from the Cloudstream project here...
        // Example: Voe(), MixDrop(), etc.
    )

    suspend fun extract(url: String): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()
        val subtitles = mutableListOf<SubtitleFile>()

        // Find the first extractor that matches the input URL
        val extractor = extractors.find { url.contains(it.mainUrl, ignoreCase = true) }
            ?: throw Exception("No suitable extractor found for URL: $url")

        // Run the extractor
        extractor.getUrl(
            url,
            null,
            { subtitle -> subtitles.add(subtitle) },
            { link -> links.add(link) }
        )

        // In a real API, you might want to return subtitles as well.
        // For this example, we'll just return the video links.
        return links
    }
}