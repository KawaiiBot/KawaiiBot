package me.alexflipnote.kawaiibot.extensions

import okhttp3.HttpUrl
import okhttp3.Request

fun Request.Builder.urlWithQueryParams(url: String, params: Map<String, String>): Request.Builder {
    val httpUrl = HttpUrl.get(url)
    val builder = httpUrl.newBuilder()

    params.keys.forEach {
        builder.setQueryParameter(it, params[it])
    }

    return url(builder.build())
}
