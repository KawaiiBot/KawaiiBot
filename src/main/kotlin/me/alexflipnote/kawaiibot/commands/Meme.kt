package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.extensions.urlBuilder
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.Arguments
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.extensions.resolveNames
import me.aurieh.ichigo.utils.StringTokenizer
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.User

@Command(description = "Create a meme out of any user!", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Meme : ICommand {

    private val memegenBase = "https://memegen.link/custom/"

    override fun run(ctx: CommandContext) {
        val user : User
        val args = ctx.argString.split('|')
        val topText: String
        val bottomText: String

        when {
            args.size == 3 -> {
                val iter = StringTokenizer.tokenizeToIterator(args[0])
                val newArgs = Arguments(iter, ctx.receivedEvent)
                val member = newArgs.asMember
                        ?: return ctx.send("Sorry, I couldn't find that member... ;-;")
                user = member.user
                topText = ctx.jda.resolveNames(args[1].split(' '))
                bottomText = ctx.jda.resolveNames(args[2].split(' '))
            }
            args.size == 2 -> {
                user = ctx.author
                topText = ctx.jda.resolveNames(args[0].split(' '))
                bottomText = ctx.jda.resolveNames(args[1].split(' '))
            }
            else ->
                return ctx.send("You must pass exactly 2 or 3 arguments delimited by the pipe char |")

        }

        RequestUtil.request {
            get()
            urlBuilder(memegenBase) {
                addPathSegment(topText)
                addPathSegment("$bottomText.jpg")
                addQueryParameter("alt", user.effectiveAvatarUrl)
                addQueryParameter("font", "impact")
            }
        }
                .thenAccept { ctx.channel.sendFile(it.closing()?.byteStream(), "meme.jpg").queue() }
                .thenException { ctx.send("I couldn't create the meme... ;-;") }
    }

}
