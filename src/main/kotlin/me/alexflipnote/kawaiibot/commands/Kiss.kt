package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission

@Command(description = "Kiss someone :3 ", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Kiss : ICommand {
    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Are you trying to kiss the void...?")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("*Kisses **${ctx.author.name}** back* ❤")
            m.user.idLong == ctx.author.idLong ->
                ctx.send("Sorry to see you alone ;-;")
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("kiss", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.channel.sendMessage(EmbedBuilder()
                            .setColor(KawaiiBot.embedColor)
                            .setDescription("**${m.user.name}**,you got a kiss from **${ctx.author.name}**")
                            .setImage(image.url)
                            .build()
                    ).queue()
                }
            }
        }
    }
}
