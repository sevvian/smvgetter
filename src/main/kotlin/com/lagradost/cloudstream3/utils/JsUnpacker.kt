package com.lagradost.cloudstream3.utils

// From https://github.com/jindrapetrik/jscall-unpacker/blob/master/src/main/java/cz/petrik/jscallunpacker/JSNice.java
// All credits to him
class JsUnpacker(private val packedJS: String) {
    fun unpack(): String? {
        var p = packedJS
        val p1 = p.substring(0, p.indexOf("}('"))
        p = p.substring(p.indexOf("}('") + 3)
        p.substring(0, p.indexOf("',") - 0)
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

private class Unpacker(payload: String, private val symtab: List<String>, private val count: Int) {
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

private class Unbase(private val count: Int) {
    private var alphabet: String? = null
    private var dictionary: Map<String, Int>? = null
    fun unbase(str: String): Int {
        var ret = 0
        if (alphabet == null) {
            val alphabet: String
            val dictionary: MutableMap<String, Int>
            if (count <= 62) {
                alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                dictionary = HashMap()
                for (i in 0 until count) {
                    dictionary[alphabet.substring(i, i + 1)] = i
                }
            } else {
                alphabet = ""
                dictionary = HashMap()
            }
            this.alphabet = alphabet
            this.dictionary = dictionary
        }
        val tmp = str.reversed()
        for (i in tmp.indices) {
            val c = tmp.substring(i, i + 1)
            val multi = Math.pow(count.toDouble(), i.toDouble()).toInt()
            ret += dictionary!![c]!! * multi
        }
        return ret
    }
}