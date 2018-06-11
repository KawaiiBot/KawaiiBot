package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.annotations.Command
import okhttp3.RequestBody

@Command(description = "Make a dank, drake meme", guildOnly = true)
class Drake : AbstractAPICommand() {
    override val path = "/image/drake"

    override fun makeBody(ctx: CommandContext): RequestBody? {
        val args = ctx.argString.split("|")
        if (args.size != 2) return failBody {
            ctx.send("You need to specify top and body text (split using `|`) ;-;")
        }

        return RequestUtil.jsonBody("top" to args[0], "bottom" to args[1])
    }
}