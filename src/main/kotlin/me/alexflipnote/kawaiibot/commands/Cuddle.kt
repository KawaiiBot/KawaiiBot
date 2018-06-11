package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Cuddle someone :3 ", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Cuddle : ICommand {
    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Are you trying to cuddle the void...?")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("*Cuddles **${ctx.author.name}** back*")
            m.user.idLong == ctx.author.idLong ->
                ctx.send("Sorry to see you alone ;-;")
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("cuddle", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.sendEmbed {
                        setDescription("**${m.user.name}**, you got a cuddle from **${ctx.author.name}**")
                        setImage(image.url)
                    }
                }
            }
        }
    }
}
