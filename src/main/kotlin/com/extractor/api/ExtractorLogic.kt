package com.extractor.api

import android.util.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import org.reflections.Reflections
import java.lang.reflect.Modifier

object ExtractorLogic {
    private const val TAG = "ExtractorLogic"

    // Use lazy initialization to scan the classpath for extractors only once.
    // This makes the service completely autonomous.
    private val extractors: List<ExtractorApi> by lazy {
        Log.i(TAG, "Initializing and discovering extractors via reflection...")
        val reflections = Reflections("com.lagradost.cloudstream3.extractors")
        val extractorClasses = reflections.getSubTypesOf(ExtractorApi::class.java)

        extractorClasses.mapNotNull { extractorClass ->
            try {
                // Ensure the class is concrete (not abstract) and has a public no-argument constructor.
                if (!Modifier.isAbstract(extractorClass.modifiers) && extractorClass.constructors.any { it.parameterCount == 0 }) {
                    extractorClass.getDeclaredConstructor().newInstance()
                } else {
                    null
                }
            } catch (e: Exception) {
                // This can happen with inner classes or classes without a default constructor. It's safe to ignore them.
                Log.w(TAG, "Could not instantiate extractor: ${extractorClass.simpleName}. Reason: ${e.message}")
                null
            }
        }.also {
            Log.i(TAG, "Discovered ${it.size} extractors: ${it.joinToString { ex -> ex.name }}")
        }
    }

    suspend fun extract(url: String): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()
        val subtitles = mutableListOf<SubtitleFile>()

        // Find the first extractor that matches the input URL from the dynamically loaded list.
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