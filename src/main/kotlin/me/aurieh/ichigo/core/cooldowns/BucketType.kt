package me.aurieh.ichigo.core.cooldowns

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

enum class BucketType {
    USER,
    CHANNEL,
    GUILD,
    GLOBAL;

    fun getKey(event: MessageReceivedEvent): Long {
        return when (this) {
            USER -> event.author.idLong
            CHANNEL -> event.channel.idLong
            GUILD -> event.guild?.idLong ?: event.channel.idLong // fall back to dm channel
            GLOBAL -> event.jda.selfUser.idLong
        }
    }
}