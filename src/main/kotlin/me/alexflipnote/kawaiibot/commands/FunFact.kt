package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.json
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Fun fact, did you know...")
class FunFact : ICommand {
    override fun run(ctx: CommandContext) {
        RequestUtil.get("https://nekos.life/api/v2/fact").thenAccept {
            val res = it.json()?.getString("fact")
            ctx.send("\uD83D\uDCDA **Fun fact:**\n" + res ?: return@thenAccept ctx.send("Bad response ;-;"))
        }.thenException { ctx.send("I-I couldn't find any facts to tell... I'm sorry ;-;") }
    }
}
