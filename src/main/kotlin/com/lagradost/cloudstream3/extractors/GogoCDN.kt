package com.lagradost.cloudstream3.extractors

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

open class GogoCDN : ExtractorApi("GogoCDN", "https://gogocdn.net", requiresReferer = true) {
    private val key = "37911490979715163134003223491201"
    private val iv = "5467413832793086"
    private val secondKey = "57413286913774904239134818786352"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val id = Regex("""id=(.*?)&""").find(response)?.groupValues?.get(1) ?: return
        val encryptedId = encrypt(id, key, iv)
        val ajaxUrl = "https://gogocdn.net/encrypt-ajax.php?id=$encryptedId&alias=$id"
        val ajaxResponse = app.get(ajaxUrl, headers = mapOf("X-Requested-With" to "XMLHttpRequest")).parsed<GogoCDNResponse>()
        val decryptedSource = decrypt(ajaxResponse.data, secondKey, iv)
        val sources = Regex("""file":"(.*?)"\}""").findAll(decryptedSource).map { it.groupValues[1].replace("\\", "") }.toList()
        sources.forEach { sourceUrl ->
            callback(
                newExtractorLink(
                    this.name,
                    this.name,
                    sourceUrl,
                    url,
                    Qualities.Unknown,
                    isM3u8 = sourceUrl.contains(".m3u8")
                )
            )
        }
    }

    private fun encrypt(text: String, key: String, iv: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key.toByteArray(), "AES"), javax.crypto.spec.IvParameterSpec(iv.toByteArray()))
        return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(text.toByteArray()))
    }

    private fun decrypt(text: String, key: String, iv: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key.toByteArray(), "AES"), javax.crypto.spec.IvParameterSpec(iv.toByteArray()))
        return String(cipher.doFinal(java.util.Base64.getDecoder().decode(text)))
    }

    data class GogoCDNResponse(
        @JsonProperty("data") val data: String
    )
}