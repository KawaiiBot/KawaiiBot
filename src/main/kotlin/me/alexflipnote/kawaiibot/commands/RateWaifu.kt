package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import java.util.*

@Command(description = "Rates your waifu~", aliases = ["rate"])
class RateWaifu : ICommand {
    var r = Random()

    override fun run(ctx: CommandContext) {
        if (!ctx.argString.isEmpty()) {
            // The random should have decimals, but idk how...
            ctx.send("I'd rate ${ctx.args.asDisplayString} a **${r.nextInt(100 - 1) + 1} / 100**")
        } else {
            ctx.send("You have to rate something..?")
        }
    }
}
