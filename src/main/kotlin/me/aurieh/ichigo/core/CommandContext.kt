package me.aurieh.ichigo.core

import me.aurieh.ichigo.utils.StringTokenizer
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory

class CommandContext internal constructor(val receivedEvent: MessageReceivedEvent, val prefix: String, private val argIter: StringTokenizer.TokenIterator, internal val commandHandler: CommandHandler) {
    val args by lazy {
        Arguments(argIter, receivedEvent)
    }
    val argString get() = args.asString

    val message: Message = receivedEvent.message

    val author: User = receivedEvent.author

    val member: Member? = receivedEvent.member

    val guild: Guild? = receivedEvent.guild

    val jda: JDA = receivedEvent.jda

    val channel: MessageChannel = receivedEvent.channel

    val textChannel: TextChannel? = receivedEvent.textChannel

    val privateChannel: PrivateChannel? = receivedEvent.privateChannel

    val isFromDM: Boolean = receivedEvent.isFromType(ChannelType.PRIVATE)

    @JvmOverloads
    fun send(response: String, success: (Message) -> Unit = {}) {
        channel.sendMessage(response).queue(success) {
            LOG.error("error while trying to send message", it)
        }
    }

    inline fun sendEmbed(block: EmbedBuilder.() -> Unit) {
        return sendEmbed(block, {})
    }

    inline fun sendEmbed(block: EmbedBuilder.() -> Unit, noinline success: (Message) -> Unit) {
        channel.sendMessage(EmbedBuilder().apply(block).build()).queue(success) {
            LOG.error("error while trying to send embed", it)
        }
    }

    inline fun<reified T : Event> waitFor(crossinline predicate: (T) -> Boolean, timeout: Int = -1): T {
        TODO("Aurieh will implement this")
    }

    fun sameContext(): ((MessageReceivedEvent) -> Boolean) {
        return { author.id == it.author.id && channel.id == it.channel.id }
    }

    companion object {
        val LOG = LoggerFactory.getLogger(this::class.java)
    }
}