package com.cncverse

import com.cncverse.UltimaMediaProvidersUtils.ServerName
import com.cncverse.UltimaMediaProvidersUtils.encodeUrl
import com.cncverse.UltimaMediaProvidersUtils.getEpisodeSlug
import com.cncverse.UltimaMediaProvidersUtils.getIndexQuality
import com.cncverse.UltimaMediaProvidersUtils.getIndexQualityTags
import com.cncverse.UltimaUtils.Category
import com.cncverse.UltimaUtils.LinkData
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorLink

class DahmerMoviesMediaProvider : MediaProvider() {
    override val name = "DahmerMovies"
    override val domain = "https://worker-mute-fog-66ae.ihrqljobdq.workers.dev"
    override val categories = listOf(Category.MEDIA)

    override suspend fun loadContent(
            url: String,
            data: LinkData,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
    ) {
        val mediaUrl =
                if (data.season == null) {
                    "$url/movies/${data.title?.replace(":", "")} (${data.year})/"
                } else {
                    "$url/tvs/${data.title?.replace(":", " -")}/Season ${data.season}/"
                }

        val request = app.get(mediaUrl, timeout = 60L)
        if (!request.isSuccessful) return
        val paths =
                request.document
                        .select("a")
                        .map { it.text() to it.attr("href") }
                        .filter {
                            if (data.season == null) {
                                it.first.contains(Regex("(?i)(1080p|2160p)"))
                            } else {
                                val (seasonSlug, episodeSlug) =
                                        getEpisodeSlug(data.season, data.episode)
                                it.first.contains(Regex("(?i)S${seasonSlug}E${episodeSlug}"))
                            }
                        }
                        .ifEmpty {
                            return
                        }

        paths.map {
            val quality = getIndexQuality(it.first)
            val tag = getIndexQualityTags(it.first)
            UltimaMediaProvidersUtils.commonLinkLoader(
                    name,
                    ServerName.Custom,
                    (mediaUrl + it.second).encodeUrl(),
                    null,
                    null,
                    subtitleCallback,
                    callback,
                    quality,
                    tag = tag
            )
        }
    }

    // #region - Encryption and Decryption handlers
    // #endregion - Encryption and Decryption handlers

    // #region - Data classes
    // #endregion - Data classes

}
