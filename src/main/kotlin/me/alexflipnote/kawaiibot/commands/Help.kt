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
        // TODO: check handler
        val canDisplayDeveloperCommands = KawaiiBot.developerIds.contains(ctx.author.idLong)

        // TODO: Unshit
        val commands = Helpers.splitText(KawaiiBot.commandHandler.commands
                .filter { command -> !command.value.properties.hidden && (!command.value.properties.developerOnly || canDisplayDeveloperCommands) }
                .map { command -> "${Helpers.pad(command.key.toLowerCase(), " ", 20)}${command.value.properties.description}" }
                .joinToString("\n"), 1950)

        val chan = ctx.author.openPrivateChannel().await()
        val actions = commands.map { chan.sendMessage("```\n$it```") }
        try {
            queueInOrder(actions)
        } catch (e: Throwable) {
            ctx.send("I'm unable to message you, **${ctx.author.name}** ;-;")
        }
    }
}