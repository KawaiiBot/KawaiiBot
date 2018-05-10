package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.json
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder

@Command(description = "Images of ducks, QUACK!")
class Duck : ICommand {
    override fun run(ctx: CommandContext) {
        RequestUtil.get("https://random-d.uk/api/v1/quack").thenAccept {
            val res = it.json() ?: return@thenAccept ctx.send("No ducks here ;-;")
            ctx.channel.sendMessage(EmbedBuilder()
                    .setColor(KawaiiBot.embedColor)
                    .setTitle("Ducks! \uD83E\uDD86")
                    .setImage(res.getString("url"))
                    .setFooter(res.getString("message"), null)
                    .build()
            ).queue()
        }.thenException { ctx.send("I-I couldn't find any ducks... I'm sorry ;-;") }
    }
}
