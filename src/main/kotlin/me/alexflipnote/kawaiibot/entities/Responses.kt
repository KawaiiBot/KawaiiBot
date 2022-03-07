package me.alexflipnote.kawaiibot.entities

import net.dv8tion.jda.api.entities.User
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Responses {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM YYYY, kk:mm")

    fun responsible(author: User, reason: String = ""): String {
        return "[ ${author.asTag} ] ${reason.ifEmpty { "No reason given." }}"
    }

    fun action(case: String): String {
        return "\u2705 Successfully **$case** the user."
    }

    fun formatDate(date: OffsetDateTime): String {
        return date.format(dateTimeFormatter)
    }
}