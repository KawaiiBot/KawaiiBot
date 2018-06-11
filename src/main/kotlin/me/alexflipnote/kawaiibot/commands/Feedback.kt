package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType

@Command(description = "Send feedback to my masters <3")
class Feedback : ICommand {
    override fun run(ctx: CommandContext) {
        val chanId = KawaiiBot.config.getProperty("feedback_channel")
                ?: return ctx.send("I.. I don't know where to post the feedback to ;-;")

        val feedbackChannel = ctx.jda.getTextChannelById(chanId)

        if (!feedbackChannel.guild.selfMember.hasPermission(feedbackChannel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
            return ctx.send("I can't send feedback, sorry ;-;")
        }

        val embed = EmbedBuilder().run {
            setDescription("\uD83D\uDCDD ${ctx.author.name}#${ctx.author.discriminator} (${ctx.author.id})")
            setColor(KawaiiBot.embedColor)
            setThumbnail(ctx.author.effectiveAvatarUrl)
            if (ctx.channel.type == ChannelType.PRIVATE) {
                addField("DM", "This has been sent in DMs.", true)
            } else {
                addField("Guild", "${ctx.guild?.name} | ${ctx.guild?.id}", true)
                addField("Channel", "#${ctx.channel.name} | ${ctx.channel.id}", true)
            }
            addField("Message", ctx.argString, false)
            build()
        }

        feedbackChannel.sendMessage(embed).queue()
        ctx.send("Thank you for the feedback, ${ctx.author.name}")
    }
}