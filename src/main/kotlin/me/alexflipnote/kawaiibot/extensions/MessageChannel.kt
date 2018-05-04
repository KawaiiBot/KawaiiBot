package me.alexflipnote.kawaiibot.extensions

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.MessageChannel
import java.io.InputStream

fun MessageChannel.sendFile(content: String, file: InputStream, filename: String) = sendFile(file, filename, MessageBuilder().setContent(content).build())!!
