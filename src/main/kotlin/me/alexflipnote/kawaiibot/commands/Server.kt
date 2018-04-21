package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.Responses
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Check info about current server", guildOnly = true)
class Server : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.sendEmbed {
            setTitle("ℹ Information about ${ctx.guild!!.name}")
            setThumbnail(ctx.guild.iconUrl)

            addField("Server name", ctx.guild.name, true)
            addField("Server ID", ctx.guild.id, true)
            addField("Members", ctx.guild.memberCache.size().toString(), true)
            addField("Bots", ctx.guild.members.filter { it.user.isBot }.size.toString(), true)
            addField("Owner", "${ctx.guild.owner.user.name}#${ctx.guild.owner.user.discriminator}", true)
            addField("Region", ctx.guild.region.getName(), true)
            addField("Created", Responses.formatDate(ctx.guild.creationTime), true)
        }
    }
}