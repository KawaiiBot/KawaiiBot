package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.closing
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Find out the weather in your area", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Weather : ICommand { // TODO: convert to AbstractAPICommand

    override fun run(ctx: CommandContext) {
        if (ctx.argString.isEmpty())
            return ctx.send("You should probably write where you want to check the weather?")

        RequestUtil.get("${KawaiiBot.config.getProperty("api_url")}/weather?location=${ctx.argString}").thenAccept {
            it.header("Authorization", KawaiiBot.config.getProperty("api_token"))
            val body = it.closing()
            if (body == null)
                ctx.send("I couldn't find this place, try again? (Maybe with **City, Country**)")
            else
                ctx.channel.sendFile(body.byteStream(), "weather.png").queue()
        }
    }

}