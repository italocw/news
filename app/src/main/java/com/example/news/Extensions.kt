package com.example.news

import okio.ByteString.Companion.encode
import java.nio.charset.Charset

fun String.toProperlyEncoding(): String {
    val charset = Charsets.ISO_8859_1
    val textNeedsToBeEncoded =
        Charset.forName(charset.name()).newEncoder().canEncode(this)
    return if (textNeedsToBeEncoded) {
        encode(charset).utf8()
    } else
        this
}
