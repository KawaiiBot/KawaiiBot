package me.aurieh.ichigo.utils

object StringUtil {
    /**
     * Cleans up content (ZWS)
     */
    fun cleanContent(str: String): String {
        return str
                .replace("@everyone", "@\u200beveryone")
                .replace("@here", "@\u200bhere")
    }

    /**
     * Cleans up content even more (ZWS-es replaces all mentions)
     */
    fun cleanerContent(str: String): String {
        return str
                .replace("@", "@\u200b")
    }

    val userMentionPattern = Regex("<@!?(\\d{17,20})>")
    val channelMentionPattern = Regex("<#(\\d{17,20})>")
    val roleMentionPattern = Regex("<@&\\d{17,20}>")
    val emotePattern = Regex("<:.+?:(\\d{17,20})>")
}
