package me.aurieh.ichigo.extensions

import me.aurieh.ichigo.utils.StringUtil.channelMentionPattern
import me.aurieh.ichigo.utils.StringUtil.emotePattern
import me.aurieh.ichigo.utils.StringUtil.roleMentionPattern
import me.aurieh.ichigo.utils.StringUtil.userMentionPattern
import net.dv8tion.jda.core.JDA

fun JDA.resolveNames(words: List<String>): String {
    val newWords = mutableListOf<String>()
    for (word in words) {
        if (word.startsWith("<@&")) { // Role
            val found = roleMentionPattern.find(word)?.groups?.get(1)
            if (found == null) { // No Role here
                newWords.add(word)
                continue
            }
            val role = getRoleById(found.value)
            if (role == null) {
                newWords.add("@unknown-role")
                continue
            }
            newWords.add("@" + role.name)
        } else if (word.startsWith("<#")) { // Channel (text)
            val found = channelMentionPattern.find(word)?.groups?.get(1)
            if (found == null) { // No Channel here
                newWords.add(word)
                continue
            }
            val channel = getTextChannelById(found.value)
            if (channel == null) {
                newWords.add("#deleted-channel")
                continue
            }
            newWords.add("#" + channel.name)
        } else if (word.startsWith("<@")) { // User
            val found = userMentionPattern.find(word)?.groups?.get(1) // Prevent boxing String?
            if (found == null) { // No User here
                newWords.add(word)
                continue
            }
            val user = getUserById(found.value)
            if (user == null) { // We don't have that user cached
                newWords.add("@Uncached#0000")
                continue
            }
            newWords.add("@${user.name}#${user.discriminator}")
        } else if (word.startsWith("<:")) { // Emote
            val found = emotePattern.find(word)?.groups?.get(1)
            if (found == null) { // No Emote here
                newWords.add(word)
                continue
            }
            val emoji = getEmoteById(found.value)
            if (emoji == null) {
                newWords.add(":unknown:")
                continue
            }
            newWords.add(":" + emoji.name + ":")
        } else {
            newWords.add(word)
        }
    }
    return newWords.joinToString(" ")
}
