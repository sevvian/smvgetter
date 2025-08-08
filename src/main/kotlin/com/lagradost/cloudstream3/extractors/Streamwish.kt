package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.base64Decode
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.fasterxml.jackson.annotation.JsonProperty

class Streamwish : ExtractorApi("Streamwish", "https://streamwish.to", requiresReferer = false) {
    private data class StreamwishJson(
        @JsonProperty("file") val file: String,
        @JsonProperty("title") val title: String,
        @JsonProperty("height") val height: String
    )

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (com.lagradost.cloudstream3.SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = referer).text
        val packed = Regex("""eval\(function\(p,a,c,k,e,d\)(.*)\)""").find(response)?.groupValues?.get(1)
        if (packed != null) {
            val decoded = JsUnpacker(packed).unpack()
            val links = Regex("""file:"(.*)"""").findAll(decoded.toString()).map { it.groupValues[1] }.toList()
            links.forEach { link ->
                callback(
                    ExtractorLink(
                        this.name,
                        this.name,
                        link,
                        url,
                        getQualityFromName(""),
                        link.contains(".m3u8")
                    )
                )
            }
        }
    }
}

// From https://github.com/jindrapetrik/jscall-unpacker/blob/master/src/main/java/cz/petrik/jscallunpacker/JSNice.java
// All credits to him
class JsUnpacker(private val packedJS: String) {
    fun unpack(): String? {
        var p = packedJS
        val p1 = p.substring(0, p.indexOf("}('"))
        p = p.substring(p.indexOf("}('") + 3)
        val p2 = p.substring(0, p.indexOf("',") - 0)
        p = p.substring(p.indexOf("',") + 2)
        val p3 = p.substring(0, p.indexOf(","))
        p = p.substring(p.indexOf(",") + 1)
        val p4 = p.substring(0, p.indexOf(").split('|')"))
        val payload = p1.replace("\\", "")
        val symtab = p4.replace("\\", "").replace("'", "").split("|")
        val count = p3.toInt()
        return Unpacker(payload, symtab, count).unpack()
    }
}

class Unpacker(payload: String, private val symtab: List<String>, private val count: Int) {
    private var payload: String
    fun unpack(): String? {
        var p = payload
        val unbase = Unbase(count)
        val regex = Regex("\\b\\w+\\b")
        p = regex.replace(p) { result ->
            val base36 = result.value
            val base10 = unbase.unbase(base36)
            if (base10 < count) {
                var value = symtab[base10]
                if (value.isEmpty()) {
                    value = base36
                }
                value
            } else {
                base36
            }
        }
        return p.replace("\"", "'")
    }

    init {
        this.payload = payload
    }
}

class Unbase(private val count: Int) {
    private var ALPHABET: String? = null
    private var DICTIONARY: Map<String, Int>? = null
    fun unbase(str: String): Int {
        var ret = 0
        if (ALPHABET == null) {
            var ALPHABET: String
            var DICTIONARY: MutableMap<String, Int>
            if (count <= 62) {
                ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                DICTIONARY = HashMap()
                for (i in 0 until count) {
                    DICTIONARY[ALPHABET.substring(i, i + 1)] = i
                }
            } else {
                ALPHABET = ""
                DICTIONARY = HashMap()
            }
            this.ALPHABET = ALPHABET
            this.DICTIONARY = DICTIONARY
        }
        val tmp = str.reversed()
        for (i in tmp.indices) {
            val c = tmp.substring(i, i + 1)
            val multi = Math.pow(count.toDouble(), i.toDouble()).toInt()
            ret += DICTIONARY!![c]!! * multi
        }
        return ret
    }
}