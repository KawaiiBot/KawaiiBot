package me.alexflipnote.kawaiibot.extensions

import okhttp3.HttpUrl
import okhttp3.Request

inline fun Request.Builder.urlBuilder(block: HttpUrl.Builder.() -> Unit): Request.Builder {
    val httpUrl = HttpUrl.Builder().apply(block).build()
    url(httpUrl)
    return this
}

inline fun Request.Builder.urlBuilder(url: String, block: HttpUrl.Builder.() -> Unit): Request.Builder {
    val builder = HttpUrl.parse(url)?.newBuilder() ?: throw Exception("Bad URL")
    builder.block()
    url(builder.build())
    return this
}
