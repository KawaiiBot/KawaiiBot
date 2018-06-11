package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.Responses
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Check when a user joined the current server", guildOnly = true, botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class JoinedAt : ICommand {
    override fun run(ctx: CommandContext) {
        val member = ctx.args.asMember ?: ctx.member!!
        ctx.sendEmbed {
            setDescription("**${member.user.name}#${member.user.discriminator}** joined **${ctx.guild!!.name}**\n${Responses.formatDate(member.joinDate)}")
            setThumbnail(member.user.effectiveAvatarUrl)
            setTimestamp(member.joinDate)
        }
    }
}