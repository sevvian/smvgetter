package android.util

import java.util.Base64 as JavaBase64

/**
 * A mock for the Android Base64 class that delegates to the standard
 * Java Base64 implementation. This is required by some extractors for
 * decoding/encoding data.
 */
object Base64 {
    const val DEFAULT = 0

    @JvmStatic
    fun decode(str: String, flags: Int): ByteArray {
        return JavaBase64.getDecoder().decode(str)
    }
    
    @JvmStatic
    fun encodeToString(input: ByteArray, flags: Int): String {
        return JavaBase64.getEncoder().encodeToString(input)
    }
}