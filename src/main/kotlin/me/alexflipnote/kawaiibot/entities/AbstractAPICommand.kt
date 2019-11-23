package me.alexflipnote.kawaiibot.entities

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import okhttp3.RequestBody

abstract class AbstractAPICommand : ICommand {
    abstract val path: String
    open val blankResponse = "You want to make a blank ${this.javaClass.simpleName.toLowerCase()}...?"

    inline fun failBody(block: () -> Unit): RequestBody? {
        block()
        return null
    }

    open fun makeArgument(ctx: CommandContext): String {
        return "text=${ctx.argString}"
    }

    open fun checkBlank(ctx: CommandContext): Boolean {
        return ctx.argString.isEmpty()
    }

    final override fun run(ctx: CommandContext) {
        if (checkBlank(ctx))
            return ctx.send(blankResponse)

        val args = makeArgument(ctx)

        RequestUtil.get("${KawaiiBot.config.getProperty("api_url")}$path?$args")
            .thenAccept {
                val body = it.body()

                when {
                    !it.isSuccessful || body == null -> {
                        KawaiiBot.LOG.error("Invalid response from API for endpoint \"$path\", code: ${it.code()}, command args: $args")
                        ctx.send("There was an error creating the image, try again later.")
                    }
                    else -> {
                        ctx.channel.sendFile(body.byteStream(), "image.png").queue()
                    }
                }

                body?.close()
            }.thenException {
                KawaiiBot.LOG.error("api call failed", it)
                ctx.send("I couldn't process the request, sorry ;-;")
            }
    }
}