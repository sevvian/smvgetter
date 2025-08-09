package com.lagradost.cloudstream3

import com.lagradost.nicehttp.Requests
import okhttp3.OkHttpClient
import java.util.Base64
import java.util.concurrent.TimeUnit

/**
 * A simplified `app` object that mimics the one in Cloudstream.
 * This provides a shared OkHttp client instance for all extractors.
 */
// Create a custom OkHttpClient with a longer timeout
val httpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

// Pass the custom client to the Requests object
val app = Requests(
    client = httpClient,
    defaultHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0"
    )
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