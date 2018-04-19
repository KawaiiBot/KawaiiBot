package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Echo!")
class Echo : ICommand {
    override fun run(ctx: CommandContext) {
        if (ctx.argString.isEmpty()) {
            ctx.send("You can't listen to the silence.")
        } else {
            ctx.send("**${ctx.author.name}**: ${ctx.args.asCleanString}")
        }
    }
}
