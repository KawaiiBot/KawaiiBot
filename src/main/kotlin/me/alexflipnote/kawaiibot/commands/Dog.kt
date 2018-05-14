package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Random dogs!")
class Dog : ICommand {
    override fun run(ctx: CommandContext) {
        RequestUtil.get("http://random.dog/woof").thenAccept {
            val r = it.closing()?.string() ?: "I couldn't fetch any dogs ;-;"
            ctx.send("https://random.dog/$r")
        }.thenException { ctx.send("I-I couldn't find any dogs... I'm sorry ;-;") }
    }
}
