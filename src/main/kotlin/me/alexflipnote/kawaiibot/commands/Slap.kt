package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission

@Command(description = "Slap someone! o//o", botPermissions = [Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES], guildOnly = true)
class Slap : ICommand {
    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Are you trying to slap a ghost...?")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("**${ctx.author.name}** we can no longer be friends ;-;")
            m.user.idLong == ctx.author.idLong ->
                ctx.send("https://media0.giphy.com/media/1M9fmo1WAFVK0/giphy.gif") // TODO: bundle this maybe
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("slap", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.channel.sendMessage(EmbedBuilder()
                            .setColor(KawaiiBot.embedColor)
                            .setDescription("**${m.user.name}**, you got a slap from **${ctx.author.name}**")
                            .setImage(image.url)
                            .build()
                    ).queue()
                }
            }
        }
    }
}