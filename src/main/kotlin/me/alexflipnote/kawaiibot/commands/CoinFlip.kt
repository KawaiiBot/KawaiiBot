package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Flip a coin!", aliases = ["flip", "coin"])
class CoinFlip : ICommand {
    private val coinSides = arrayOf("Heads", "Tails")

    override fun run(ctx: CommandContext) {
        val choose = Helpers.chooseRandom(coinSides)
        ctx.send("**${ctx.author.name}** flipped a coin and got **$choose**!")
    }
}
