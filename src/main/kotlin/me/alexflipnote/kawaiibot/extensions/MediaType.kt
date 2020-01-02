package me.alexflipnote.kawaiibot.extensions

import okhttp3.MediaType

fun MediaType.APPLICATION_JSON(): MediaType = okhttp3.MediaType.parse("application/json")!!
