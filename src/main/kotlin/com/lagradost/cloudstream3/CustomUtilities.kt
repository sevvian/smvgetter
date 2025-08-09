package com.lagradost.cloudstream3

import com.lagradost.nicehttp.Requests
import java.util.Base64

/**
 * A simplified `app` object that mimics the one in Cloudstream.
 * This provides a shared OkHttp client instance for all extractors.
 */
val app = Requests(
    defaultHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0"
    ),
    timeout = 30_000 // Increased timeout to 30 seconds
)

/**
 * Utility function required by some extractors.
 */
fun String.base64Decode(): String {
    return try {
        String(Base64.getDecoder().decode(this))
    } catch (e: Exception) {
        ""
    }
}

fun String.base64UrlDecode(): String {
    return try {
        String(Base64.getUrlDecoder().decode(this))
    } catch (e: Exception) {
        ""
    }
}