package me.alexflipnote.kawaiibot.extensions

import me.alexflipnote.kawaiibot.utils.RequestUtil
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

fun Response.json(): JSONObject? {
    return JSONObject(closing()?.string() ?: return null)
}

fun Response.jsonArray(): JSONArray? {
    return JSONArray(closing()?.string() ?: return null)
}

fun Response.closing(): RequestUtil.ClosingResponseBody? {
    return RequestUtil.ClosingResponseBody(body() ?: return null)
}