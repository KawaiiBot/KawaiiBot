package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import java.util.*

@Command(description = "Rolls a number between the given range")
class Roll : ICommand {

    private val random = Random()

    override fun run(ctx: CommandContext) {
        var upperBound = ctx.args.nextIntOrNull() ?: 10
        var lowerBound = ctx.args.nextIntOrNull() ?: 1

        if (upperBound == lowerBound)
            return ctx.send("I can't roll between those numbers ;-;")

        if (lowerBound > upperBound) {
            val temp = upperBound
            upperBound = lowerBound
            lowerBound = temp
        }

        if (upperBound > 1000000)
            return ctx.send("You can't roll higher than 1,000,000 ;-;")

        if (lowerBound < -1000000)
            return ctx.send("You can't roll lower than -1,000,000 ;-;")

        val rolled = random.nextInt(upperBound - lowerBound) + lowerBound
        ctx.send("**${ctx.author.name}** rolled $lowerBound-$upperBound and got **$rolled**")
    }

}
