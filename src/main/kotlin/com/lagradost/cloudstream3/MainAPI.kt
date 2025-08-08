package com.lagradost.cloudstream3

import com.fasterxml.jackson.annotation.JsonProperty

// Copied from Cloudstream's MainAPI.kt to provide necessary data models.

/**
* For use in MainAPI. It's a link to a stream, with an url.
* The name is a unique identifier for the stream, for example "Streamtape" or "Doodstream"
* The source is the name of the site, this is for the UI.
* The quality is an enum, see Qualities for more info.
* The url is the direct link to the video file, but can also be a link to a site, that will be extracted.
* isM3u8 tells the player if the file is a m3u8 file, which requires a different player.
* The headers are for the player. For example if the site requires a referer.
* */
data class ExtractorLink(
    @JsonProperty("source") val source: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("referer") val referer: String,
    @JsonProperty("quality") val quality: Int,
    @JsonProperty("isM3u8") val isM3u8: Boolean = false,
    @JsonProperty("headers") val headers: Map<String, String> = mapOf(),
)

/**
* Lang should be a 3 letter language code, see https://en.wikipedia.org/wiki/List_of_ISO_639-2_codes
* */
data class SubtitleFile(
    @JsonProperty("lang") val lang: String,
    @JsonProperty("url") val url:String,
)

/**
* The different qualities. The value is an integer, because it's easier to compare.
* The higher the value, the better the quality.
* */
object Qualities {
    const val Unknown = 0
    const val P144 = 1
    const val P240 = 2
    const val P360 = 3
    const val P480 = 4
    const val P720 = 5
    const val P1080 = 6
    const val P1440 = 7 // 2K
    const val P2160 = 8 // 4K
}