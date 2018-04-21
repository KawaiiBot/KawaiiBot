package me.alexflipnote.kawaiibot.entities

import net.dv8tion.jda.core.entities.User
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.swing.text.DateFormatter

object Responses {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM YYYY, kk:mm")

    fun responsible(author: User, reason: String = ""): String {
        return "[ ${author.name}#${author.discriminator} ] ${if (reason.isEmpty()) "No reason given." else reason}"
    }

    fun action(case: String): String {
        return "\u2705 Successfully **$case** the user."
    }

    fun formatDate(date: OffsetDateTime): String {
        return date.format(dateTimeFormatter)
    }
}