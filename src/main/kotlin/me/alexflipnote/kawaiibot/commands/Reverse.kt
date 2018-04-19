package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.utils.StringUtil

@Command(description = "!poow ,ffuts esreveR")
class Reverse : ICommand {

    override fun run(ctx: CommandContext) {
        if (ctx.args.bySpace.isEmpty()) {
            ctx.send("Maybe type something to reverse?")
        } else {
            ctx.send("\uD83D\uDD01 ${StringUtil.cleanerContent(ctx.args.asDisplayString.reversed())}")
        }
    }

}
