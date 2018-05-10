package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.json
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Posts a random neko")
class Neko : ICommand {
    override fun run(ctx: CommandContext) {
        RequestUtil.get("https://nekos.life/api/neko").thenAccept {
            val res = it.json()?.getString("neko")
            ctx.send(res ?: return@thenAccept ctx.send("I couldn't receive any cats ;-;"))
        }.thenException { ctx.send("I-I couldn't find any nekos... I'm sorry ;-;") }
    }
}
