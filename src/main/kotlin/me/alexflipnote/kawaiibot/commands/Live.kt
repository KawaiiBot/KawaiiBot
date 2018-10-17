package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.json
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.Helpers
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission
import java.net.URLEncoder

@Command(description = "Check if someone's streaming!", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Live : ICommand {

    private val twitchApi: String = "https://api.twitch.tv/kraken/"

    override fun run(ctx: CommandContext) {
        if (ctx.argString.isEmpty())
            return ctx.send("I don't know who to look for...")

        val clientId = KawaiiBot.config.getProperty("twitch")
                ?: return ctx.send("I can't make requests to Twitch...")

        RequestUtil.get("${twitchApi}streams/${URLEncoder.encode(ctx.argString, "utf-8")}") {
            header("Client-ID", clientId)
        }.thenAccept {
            val json = it.json()
                    ?: return@thenAccept ctx.send("I didn't get a valid response from Twitch ;-;")

            if (!Helpers.keyExists(json, "stream")) {
                ctx.send("**${ctx.args.asCleanerString}** either doesn't exist or isn't streaming right now...")
            } else {
                val tStream = json.getJSONObject("stream")
                val channel = tStream.getJSONObject("channel")

                val status = channel.optString("status", "No Title")
                val game = tStream.optString("game", null)

                val viewers = tStream.getInt("viewers")
                val word = if (viewers == 1) "viewer" else "viewers"

                ctx.sendEmbed {
                    setTitle(ctx.args.asDisplayString)
                    setDescription("[**$status**](${channel.getString("url")}) (**$viewers** $word)\n\n"
                            + if (game != null) "Playing **$game**" else "")
                    setThumbnail(channel.optString("logo", ""))
                }
            }
        }.thenException { ctx.send("I didn't get a response from Twitch...") }
    }
}
