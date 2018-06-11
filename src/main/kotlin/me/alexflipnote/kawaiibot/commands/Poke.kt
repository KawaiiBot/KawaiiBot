package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission


@Command(description = "Poke someone :3", botPermissions = [Permission.MESSAGE_EMBED_LINKS], guildOnly = true)
class Poke : ICommand {
    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Are you trying to poke thin air...?")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("Don't poke me ;-;")
            m.user.idLong == ctx.author.idLong ->
                ctx.send("You can't poke yourself... baka ;-;")
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("poke", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.sendEmbed {
                        setDescription("**${m.effectiveName}**, you got a poke from **${ctx.author.name}**")
                        setImage(image.url)
                    }
                }
            }
        }
    }
}
