package com.cncverse

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson
import com.lagradost.api.Log
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.USER_AGENT
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.M3u8Helper.Companion.generateM3u8
import java.net.URLEncoder

class Videostr : ExtractorApi() {
    override val name = "Videostr"
    override val mainUrl = "https://videostr.net"
    override val requiresReferer = false

    @SuppressLint("NewApi")
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val headers = mapOf(
            "Accept" to "*/*",
            "X-Requested-With" to "XMLHttpRequest",
            "Referer" to mainUrl
        )


        val id = url.substringAfterLast("/").substringBefore("?")
        val responsenonce= app.get(url, headers = headers).text
        val match1 = Regex("""\b[a-zA-Z0-9]{48}\b""").find(responsenonce)
        val match2 = Regex("""\b([a-zA-Z0-9]{16})\b.*?\b([a-zA-Z0-9]{16})\b.*?\b([a-zA-Z0-9]{16})\b""").find(responsenonce)

        val nonce = match1?.value ?: match2?.let {
            it.groupValues[1] + it.groupValues[2] + it.groupValues[3]
        }

        Log.e("Megacloud", "MegacloudResponse nonce: $nonce")
        val apiUrl = "$mainUrl/embed-1/v3/e-1/getSources?id=$id&_k=$nonce"
        Log.e("Megacloud", apiUrl)

        val gson = Gson()


        val response = try {
            val json = app.get(apiUrl, headers).text
            gson.fromJson(json, MegacloudResponse::class.java)
        } catch (e: Exception) {
            Log.e("Megacloud", "Failed to parse MegacloudResponse: ${e.message}")
            null
        } ?: return
        Log.e("Megacloud", "Failed to parse Megakey: ${response}")

        val encoded = response.sources
        val key = try {
            val keyJson = app.get("https://raw.githubusercontent.com/yogesh-hacker/MegacloudKeys/refs/heads/main/keys.json").text
            gson.fromJson(keyJson, Megakey::class.java)?.vidstr
        } catch (e: Exception) {
            Log.e("Megacloud", "Failed to parse Megakey: ${e.message}")
            null
        }

        val m3u8: String = if (".m3u8" in encoded) {
            encoded
        } else {
            val decodeUrl = "https://script.google.com/macros/s/AKfycbxHbYHbrGMXYD2-bC-C43D3njIbU-wGiYQuJL61H4vyy6YVXkybMNNEPJNPPuZrD1gRVA/exec"

            val fullUrl = buildString {
                append(decodeUrl)
                append("?encrypted_data=").append(URLEncoder.encode(encoded, "UTF-8"))
                append("&nonce=").append(URLEncoder.encode(nonce, "UTF-8"))
                append("&secret=").append(URLEncoder.encode(key, "UTF-8"))
            }

            val decryptedResponse = app.get(fullUrl).text
            Regex("\"file\":\"(.*?)\"")
                .find(decryptedResponse)
                ?.groupValues?.get(1)
                ?: throw Exception("Video URL not found in decrypted response")
        }


        val m3u8headers = mapOf(
            "Referer" to "https://videostr.net/",
            "Origin" to "https://videostr.net/"
        )

        try {
            M3u8Helper.generateM3u8(name, m3u8, mainUrl, headers = m3u8headers).forEach(callback)
        } catch (e: Exception) {
            Log.e("Megacloud", "Error generating M3U8: ${e.message}")
        }

        response.tracks.forEach { track ->
            if (track.kind == "captions" || track.kind == "subtitles") {
                subtitleCallback(
                    SubtitleFile(
                        track.label,
                        track.file
                    )
                )
            }
        }
    }

    data class MegacloudResponse(
        val sources: String,
        val tracks: List<Track>,
        val encrypted: Boolean,
        val intro: Intro,
        val outro: Outro,
        val server: Long,
    )

    data class Track(
        val file: String,
        val label: String,
        val kind: String,
        val default: Boolean?,
    )

    data class Intro(
        val start: Long,
        val end: Long,
    )

    data class Outro(
        val start: Long,
        val end: Long,
    )


    data class Megakey(
        val rabbit: String,
        val mega: String,
        val vidstr: String
    )
}
