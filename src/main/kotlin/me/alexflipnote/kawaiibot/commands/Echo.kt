package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Echo!")
class Echo : ICommand {
    override fun run(ctx: CommandContext) {
        if (ctx.argString.isEmpty()) {
            ctx.send("Nothing will echo if you don't give me something to say... ;-;")
        } else {
            ctx.send("**${ctx.author.name}**: ${ctx.args.asCleanerString}")
        }
    }
}
