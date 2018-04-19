package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.utils.StringUtil

@Command(description = "Give someone a cookie! \uD83C\uDF6A", guildOnly = true)
class Cookie : ICommand {
    override fun run(ctx: CommandContext) {
        val member = ctx.args.nextMemberOrNull()
                ?: return ctx.send("Why are you trying to give a cookie to thin air?")

        val builder = StringBuilder("**${member.user.name}**, you got a \uD83C\uDF6A from **${ctx.author.name}**")
        val reason = ctx.args.collect()
        if (reason.isEmpty())
            builder.append("\n")
        else
            builder.append("\n\n**Reason:** ${StringUtil.cleanContent(reason)}") // TODO: mentions
        builder.append("\n(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ \uD83C\uDF6A")
        ctx.send(builder.toString())
    }
}