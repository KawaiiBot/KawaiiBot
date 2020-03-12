package me.aurieh.ichigo.core

import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.checks.ICheck
import me.aurieh.ichigo.utils.OptParser
import me.aurieh.ichigo.utils.StringTokenizer
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import java.util.*


class CommandHandler private constructor(val commands: Map<String, CommandWrapper>, val aliases: Map<String, String>, val prefix: String, private val checks: Set<ICheck>, private val developers: Set<Long>) : ListenerAdapter() {
    private var readyFired = false

    private val prefixes = mutableListOf(prefix)
    internal val coroutineContext by lazy { newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors() / 2, "CommandHandlerPool") }

    override fun onReady(event: ReadyEvent) {
        if (!readyFired) {
            readyFired = true
            prefixes.add("<@${event.jda.selfUser.id}> ")
            prefixes.add("<@!${event.jda.selfUser.id}> ")
        }
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        val welcomeChannel = event.guild.textChannels.firstOrNull { it.canTalk() }
            ?: return

        welcomeChannel.sendMessage("Hello everyone, t-thanks for adding me :3\n" +
            "I hope I can do good on t-this server ❤\n" +
            "Use **+help** to see what I can do for you~\n" +
            "If you want help from my masters, join here: **<https://discord.gg/wGwgWJW>**\n" +
            "or follow my Twitter here **<https://twitter.com/KawaiiDiscord>** ;3").queue()
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.isWebhookMessage || event.author.isFake || event.author.isBot) {
            return
        }

        if (event.channelType.isGuild) { // Guild checks
            if (!event.guild.isAvailable) return
            if (!event.textChannel.canTalk()) return
        }

        val content = event.message.contentRaw
        val trigger = prefixes.firstOrNull { content.startsWith(it) } ?: return
        val withoutTrigger = content.substring(trigger.length).trim()

        if (withoutTrigger.isEmpty()) {
            return
        }

        val iter = StringTokenizer.tokenizeToIterator(withoutTrigger)
        if (!iter.hasNext()) return
        val label = try {
            iter.next()
        } catch (e: StringTokenizer.UnclosedQuoteError) {
            return
        }
        val command = commands[label] ?: commands[aliases[label]] ?: return

        // -- CHECKS: don't touch this --
        val bucket = command.bucket
        if (bucket != null) { // can't use nullsafe because Long? is boxed
            val retryAfter = bucket.update(event)
            if (retryAfter > 0) {
                send(event, "**Sorry, this command is on cooldown for $retryAfter milliseconds**")
                return
            }
        }
        if (command.properties.developerOnly && !developers.contains(event.author.idLong)) {
            send(event, "**Sorry, you have to be a developer to use this command**")
            return
        }
        if (command.properties.guildOnly && !event.channelType.isGuild) {
            send(event, "**You cannot use this command in DMs!**")
            return
        }
        if (command.properties.isNSFW && (event.channelType.isGuild && !event.textChannel.isNSFW)) {
            send(event, "**You can only use this command in NSFW or DM channels**")
            return
        }
        val missingSelfPermissions = command.canSelfInteract(event)
        if (missingSelfPermissions.isNotEmpty()) {
            val fmted = missingSelfPermissions.joinToString(", ") { it.getName() }
            send(event, "**I'm missing some permissions ;-;**\n$fmted")
            return
        }
        val missingUserPermissions = command.canUserInteract(event)
        if (missingUserPermissions.isNotEmpty()) {
            val fmted = missingUserPermissions.joinToString(", ") { it.getName() }
            send(event, "**You're missing some permissions ;-;**\n$fmted")
            return
        }
        if (!checks.all { it.pass(event, command, iter) }) return
        if (!command.checks.all { it.pass(event, command, iter) }) return
        // -- CHECKS --

        if (command.properties.isNSFW) {
            ++KawaiiBot.pornUsage
        } else {
            ++KawaiiBot.otherCommandUsage
        }

        val ctx = CommandContext(event, prefix, iter, this)

        LOG.debug("${event.author} invoked ${command.name} with content: ${event.message.contentRaw}")

        try {
            command.run(ctx)
        } catch (e: Arguments.InvalidArgumentError) {
            ctx.send("**You have an invalid/missing argument:**\n```${e.message}```")
        } catch (e: OptParser.EmptyKeyError) {
            ctx.send("**You have an empty flag key**")
        } catch (e: StringTokenizer.UnclosedQuoteError) {
            ctx.send(UNCLOSED_QUOTE_ERROR)
        } catch (e: Throwable) {
            LOG.error("command runner raised an uncaught exception", e)
        }
    }

    private fun send(event: MessageReceivedEvent, msg: String) {
        event.channel
                .sendMessage(msg)
                .queue(null) {
                    LOG.error("unexpected error while sending termination response", it)
                }
    }

    class Builder(private val prefix: String) {
        private val commands = hashMapOf<String, CommandWrapper>()
        private val aliases = hashMapOf<String, String>()
        private val checks = HashSet<ICheck>()
        private val developers = HashSet<Long>()

        fun addCommand(commandWrapper: CommandWrapper): Builder {
            val name = commandWrapper.name
            if (commands.containsKey(name)) {
                throw RuntimeException("Duplicate command key: $name")
            }
            commands[name] = commandWrapper
            commandWrapper.properties.aliases.forEach {
                if (commands.containsKey(it)) {
                    throw RuntimeException("Duplicate alias key: $it")
                }
                aliases[it] = name
            }
            return this
        }

        fun addCommandsAll(collection: Collection<CommandWrapper>): Builder {
            collection.forEach { addCommand(it) }
            return this
        }

        fun addAlias(alias: String, to: String): Builder {
            aliases[alias] = to
            return this
        }

        fun addAliasesAll(collection: Collection<Pair<String, String>>): Builder {
            collection.forEach { (alias, to) -> addAlias(alias, to) }
            return this
        }

        fun addCheck(check: ICheck): Builder {
            checks.add(check)
            return this
        }

        fun addChecksAll(collection: Collection<ICheck>): Builder {
            checks.addAll(collection)
            return this
        }

        fun addDeveloper(id: Long): Builder {
            developers.add(id)
            return this
        }

        fun addDevelopersAll(collection: Collection<Long>): Builder {
            developers.addAll(collection)
            return this
        }

        fun build(): CommandHandler {
            return CommandHandler(commands, aliases, prefix, checks, developers)
        }
    }

    companion object {
        internal val LOG = LoggerFactory.getLogger(this::class.java)!!
        internal const val UNCLOSED_QUOTE_ERROR = "" +
                "**You just passed an unclosed quote. " +
                "If you are trying to use a quote as an apostrophe, escape it twice, like so: " +
                "`\\\\\\\\'`**"
    }
}