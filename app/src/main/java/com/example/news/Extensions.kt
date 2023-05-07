package com.example.news

import okio.ByteString.Companion.encode
import java.nio.charset.Charset

fun String.toProperlyEncoding(): String {
    val textCanBeEncondedToISO_8859_1 =
        Charset.forName(Charsets.ISO_8859_1.name()).newEncoder().canEncode(this)

    return if (textCanBeEncondedToISO_8859_1) {
       val textFromISO = encode(Charsets.ISO_8859_1).utf8()
            if (textFromISO.length < this.length) {
                textFromISO
            } else {
                this
        }
    } else {
        this
    }
}
