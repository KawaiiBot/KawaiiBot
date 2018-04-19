package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.Permission

@Command(description = "Give to someone a flower :3", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Flower : ICommand {
    private val flowers = arrayOf("\uD83C\uDF37", "\uD83C\uDF3C", "\uD83C\uDF38", "\uD83C\uDF3A", "\uD83C\uDF3B", "\uD83C\uDF39")

    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Why are you trying to give the floor a flower ?")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("*Awww*")
            else -> {
                val msg = MessageBuilder()
                        .setContent("**${m.user.name}** you got a ${Helpers.chooseRandom(flowers)} from **${ctx.author.name}**")
                        .build()
                ctx.channel
                        .sendFile(Helpers.getImageStream("images/flower.gif"), "flower.gif", msg)
                        .queue()
            }
        }
    }
}
