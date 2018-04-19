package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.extensions.urlBuilder
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.extensions.resolveNames
import net.dv8tion.jda.core.Permission

@Command(description = "Create a meme out of any user!", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Meme : ICommand {

    private val memegenBase = "https://memegen.link/custom/"

    override fun run(ctx: CommandContext) {
        val args = ctx.argString.split('|')
        if (args.size != 2)
            return ctx.send("You must pass exactly 2 arguments delimited by the pipe char |")

        RequestUtil.request {
            get()
            urlBuilder(memegenBase) {
                addPathSegment(ctx.jda.resolveNames(args[0].split(' ')))
                addPathSegment(ctx.jda.resolveNames(args[1].split(' ')) + ".jpg")
                addQueryParameter("alt", ctx.author.effectiveAvatarUrl)
                addQueryParameter("font", "impact")
            }
        }
                .thenAccept { ctx.channel.sendFile(it.closing()?.byteStream(), "meme.jpg").queue() }
                .thenException { ctx.send("I couldn't create the meme... ;-;") }
    }

}
