package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Find out the weather in your area")
class Weather : ICommand { // TODO: convert to AbstractAPICommand

    override fun run(ctx: CommandContext) {
        if (ctx.argString.isEmpty())
            return ctx.send("You should probably write where you want to check the weather?")

        RequestUtil.post("${KawaiiBot.config.getProperty("api_url")}/image/weather", RequestUtil.jsonBody("text" to ctx.argString)).thenAccept {
            val body = it.closing()
            if (body == null)
                ctx.send("I couldn't find this place, try again? (Maybe with **City, Country**)")
            else
                ctx.channel.sendFile(body.byteStream(), "weather.png").queue()
        }
    }

}