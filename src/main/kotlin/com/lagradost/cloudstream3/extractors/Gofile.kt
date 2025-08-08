package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.serialization.Serializable

class Gofile : ExtractorApi("Gofile", "https://gofile.io", requiresReferer = false) {
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val id = url.substringAfterLast("/")
        val token = app.get("https://api.gofile.io/createAccount").parsed<GofileToken>().data?.token
        val response = app.get("https://api.gofile.io/getContent?contentId=$id&token=$token&websiteToken=7fd94ds12fds4", referer = url).parsed<GofileResponse>()
        response.data?.contents?.forEach { (_, value) ->
            callback(
                newExtractorLink(
                    this.name,
                    value.name,
                    value.link,
                    url,
                    getQualityFromName(""),
                )
            )
        }
    }

    @Serializable
    data class GofileToken(
        @JsonProperty("data") val data: GofileTokenData? = null
    )

    @Serializable
    data class GofileTokenData(
        @JsonProperty("token") val token: String
    )

    @Serializable
    data class GofileResponse(
        @JsonProperty("data") val data: GofileData? = null
    )

    @Serializable
    data class GofileData(
        @JsonProperty("contents") val contents: Map<String, GofileContent>? = null
    )

    @Serializable
    data class GofileContent(
        @JsonProperty("name") val name: String,
        @JsonProperty("link") val link: String
    )
}