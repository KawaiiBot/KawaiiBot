package me.aurieh.ichigo.core.checks

import me.aurieh.ichigo.core.CommandWrapper
import me.aurieh.ichigo.utils.StringTokenizer
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

interface ICheck {
    fun pass(receivedEvent: MessageReceivedEvent, commandWrapper: CommandWrapper, args: StringTokenizer.TokenIterator): Boolean
}