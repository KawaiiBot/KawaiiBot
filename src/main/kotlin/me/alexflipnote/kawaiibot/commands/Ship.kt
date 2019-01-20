package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Make a lovely ship <3", guildOnly = true, botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Ship : ICommand { // TODO: convert to AbstractAPICommand
    override fun run(ctx: CommandContext) {
        val first = ctx.args.nextMemberOrNull()?.user ?: return ctx.send("You must select 2 members")
        val second = ctx.args.nextMemberOrNull()?.user ?: return ctx.send("You must select 2 members")
        if (first.idLong == second.idLong)
            return ctx.send("You can't ship someone with themselves")

        val firstPart = first.name.substring(0, first.name.length / 2)
        val secondPart = second.name.substring(second.name.length / 2)
        val avatars = "?user=${first.effectiveAvatarUrl}&user2=${second.effectiveAvatarUrl}"
        RequestUtil.get("${KawaiiBot.config.getProperty("api_url")}/ship$avatars").thenAccept {
            val inputStream = it.closing()?.byteStream() ?: return@thenAccept ctx.send("Got empty response")
            ctx.channel.sendMessage("Lovely shipping~\nShip name: **$firstPart$secondPart**")
                    .addFile(inputStream, "ship.png")
                    .queue()
        }
    }
}
