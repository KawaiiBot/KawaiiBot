package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Call someone a baka", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Baka : ICommand {
    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Who are you calling a baka...?")
             m.user.idLong == ctx.author.idLong ->
                ctx.channel.sendFile(Helpers.getImageStream("images/selfbaka.jpg"), "selfbaka.jpg").queue()
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("baka", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.sendEmbed {
                        setDescription("**${m.user.name}**,called **${ctx.author.name}** a baka.")
                        setImage(image.url)
                    }
                }
            }
        }
    }
}
