package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Displays your avatar", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Avatar : ICommand {
    override fun run(ctx: CommandContext) {
        val member = ctx.args.asMember?.user ?: ctx.author

        ctx.sendEmbed {
            setDescription("${member.name}'s Avatar\n[Full Image](${member.effectiveAvatarUrl})")
            setThumbnail(member.effectiveAvatarUrl)
        }
    }
}