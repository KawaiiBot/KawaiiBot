package me.alexflipnote.kawaiibot.extensions

import me.aurieh.ichigo.utils.StringUtil

fun String.clean(): String {
    return StringUtil.cleanContent(this)
}

fun String.cleaner(): String {
    return StringUtil.cleanerContent(this)
}