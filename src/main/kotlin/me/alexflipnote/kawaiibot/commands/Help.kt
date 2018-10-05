package me.alexflipnote.kawaiibot.commands

import kotlinx.coroutines.experimental.Unconfined
import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.CoroutineCommand
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.extensions.await
import me.aurieh.ichigo.extensions.queueInOrder

@Command(description = "Displays all of my available commands~", aliases = ["commands"])
class Help : CoroutineCommand(Unconfined) {
    override suspend fun execute(ctx: CommandContext) {
        if (ctx.argString.isEmpty()) {
            val canDisplayDeveloperCommands = KawaiiBot.developerIds.contains(ctx.author.idLong)

            val commands = Helpers.splitText(KawaiiBot.commandHandler.commands
                    .filter { command -> !command.value.properties.hidden && (!command.value.properties.developerOnly || canDisplayDeveloperCommands) }
                    .toSortedMap()
                    .map { command -> "${Helpers.pad(command.key.toLowerCase(), " ", 20)}${command.value.properties.description}" }
                    .joinToString("\n"), 1950)

            val chan = ctx.author.openPrivateChannel().await()
            val actions = commands.map { chan.sendMessage("```\n$it```") }
            try {
                queueInOrder(actions)
            } catch (e: Throwable) {
                return ctx.send("I'm unable to message you, **${ctx.author.name}** ;-;")
            }
        } else {
            val argLower = ctx.argString.toLowerCase()

            val command = KawaiiBot.commandHandler.commands[argLower]
                    ?: KawaiiBot.commandHandler.commands.values.firstOrNull { it.properties.aliases.contains(argLower) }
                    ?: return ctx.send("That doesn't seem to be a command ;-;")

            val aliases = command.properties.aliases.joinToString("|")
            val triggers = if (aliases.isEmpty()) {
                command.name
            } else {
                "[${command.name}|$aliases]"
            }

            val chan = ctx.author.openPrivateChannel().await()
            val firstLine = "${KawaiiBot.commandHandler.prefix}$triggers"
            chan.sendMessage("```\n$firstLine\n\n${command.properties.description}\n\nUsage:${command.properties.usage}```").queue()
        }

        ctx.message.addReaction("✉").queue()
    }
}