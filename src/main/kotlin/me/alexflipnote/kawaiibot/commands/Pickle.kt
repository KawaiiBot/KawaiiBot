package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import java.text.DecimalFormat
import java.util.*

@Command(description = "Find out your pickle size!", guildOnly = true)
class Pickle : ICommand {

    private val dpFormatter = DecimalFormat("0.00")

    override fun run(ctx: CommandContext) {
        val random = Random()
        val m = ctx.args.asMember

        if (m == null) {
            random.setSeed(ctx.author.idLong)
            val size = dpFormatter.format(random.nextInt(50).toDouble() / 1.17)
            ctx.send("**${ctx.author.name}**, your pickle size is **${size}cm** \uD83C\uDF80")
        } else {
            random.setSeed(m.user.idLong)
            val size = dpFormatter.format(random.nextInt(50).toDouble() / 1.17)
            ctx.send("**${m.user.name}'s** pickle size is **${size}cm** \uD83C\uDF80")
        }
    }
}
