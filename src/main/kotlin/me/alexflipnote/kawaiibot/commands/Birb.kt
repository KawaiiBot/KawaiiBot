package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Cute birbs :3")
class Birb : ICommand {
    override fun run(ctx: CommandContext) {
        RequestUtil.get("https://random.birb.pw/tweet").thenAccept {
            ctx.send("https://random.birb.pw/img/${it.closing()?.string()}")
        }.thenException {
            ctx.send("I-I couldn't find any birbs... I'm sorry ;-;")
        }
    }
}
