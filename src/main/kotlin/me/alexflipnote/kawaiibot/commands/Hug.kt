package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.clean
import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.Permission

@Command(description = "Give someone a hug o////o", botPermissions = [Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES])
class Hug : ICommand {
    override fun run(ctx: CommandContext) {
        val m = ctx.args.asMember

        when {
            m == null ->
                ctx.send("Are you trying to hug thin air...?")
            m.user.idLong == ctx.jda.selfUser.idLong ->
                ctx.send("*Hugs **${ctx.author.name}** back* ❤")
            m.user.idLong == ctx.author.idLong -> {
                val msg = MessageBuilder().setContent("Sorry to see you alone...").build()
                ctx.channel.sendFile(Helpers.getImageStream("images/selfhug.gif"), "selfhug.gif", msg).queue()
            }
            else -> {
                val api = KawaiiBot.wolkeApi
                api.getRandomImage("hug", null, null, NsfwFilter.NO_NSFW, null).async { image ->
                    ctx.sendEmbed {
                        setDescription("**${m.effectiveName.clean()}**, you got a hug from **${ctx.author.name}**")
                        setImage(image.url)
                    }
                }
            }
        }
    }
}
