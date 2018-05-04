package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission

@Command(description = "Lick someone o////o", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Lick : ICommand {

    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("https://i.imgur.com/UPlNJs8.gif")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("${ctx.author.name}... w-why do you lick me ;-;")
            m.user.idLong == ctx.author.idLong ->
                // TODO: download and bundle this image
                ctx.send("https://blog.eat24.com/wp-content/uploads/2016/03/cat-licking-itself-.gif")
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("lick", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.channel.sendMessage(EmbedBuilder()
                            .setColor(KawaiiBot.embedColor)
                            .setDescription("**${m.effectiveName}**, was licked by **${ctx.author.name}**")
                            .setImage(image.url)
                            .build()
                    ).queue()
                }
            }
        }
    }

}
