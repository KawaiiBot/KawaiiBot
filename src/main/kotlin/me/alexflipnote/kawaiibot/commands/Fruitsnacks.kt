package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Give someone a fruit", aliases = ["fruit"])
class Fruitsnacks : ICommand {
    private val fruits = listOf("\uD83C\uDF4F", "\uD83C\uDF4E ", "\uD83C\uDF50", "\uD83C\uDF4A", "\uD83C\uDF4B", "\uD83C\uDF4C", "\uD83C\uDF49", "\uD83C\uDF47", "\uD83C\uDF53", "\uD83C\uDF48", "\uD83C\uDF52", "\uD83C\uDF51")

    override fun run(ctx: CommandContext) {
        val member = ctx.args.asMember ?: return ctx.send("Are you trying to give a fruit to the air...?")
        val fruit = Helpers.chooseRandom(fruits)
        ctx.send("**${member.user.name}** You got a $fruit from ${ctx.author.name}\n\n(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ $fruit")
    }
}
