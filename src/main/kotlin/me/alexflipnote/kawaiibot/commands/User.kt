package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.Responses
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Get user information", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class User : ICommand {
    override fun run(ctx: CommandContext) {
        val user = ctx.args.asUser ?: ctx.author
        val member = ctx.guild?.getMember(user)

        ctx.sendEmbed {
            val fullName = "${user.name}#${user.discriminator}"
            setTitle("ℹ About **${user.id}**")
            setThumbnail(user.effectiveAvatarUrl)

            addField("Full name", fullName, true)
            addField("Nickname", member?.nickname ?: "None", true)
            addField("Account created", Responses.formatDate(user.creationTime), true)
            if (member != null) addField("Joined this server", Responses.formatDate(member.joinDate), true)
        }
    }
}