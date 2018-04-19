package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Pong!", aliases = ["beep"])
class Ping : ICommand {
    override fun run(ctx: CommandContext) {
        val start = System.currentTimeMillis()

        ctx.send("BEEP!") { m ->
            val end = System.currentTimeMillis()
            m.editMessage("WS: ${ctx.jda.ping}ms | REST: ${end - start}ms").queue()
        }
    }
}