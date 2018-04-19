package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Posts an invite to my home")
class BotServer : ICommand {
    override fun run(ctx: CommandContext) {
        if (ctx.guild == null || ctx.guild.idLong != 348267161967394826L)
        // DM or server other than KawaiiBot Hangout
            ctx.send("**Here you go ${ctx.author.name} \uD83C\uDF80\n<https://discord.gg/wGwgWJW>**")
        else
            ctx.send("**${ctx.author.name}** this is my home you know~ ❤")
    }
}
