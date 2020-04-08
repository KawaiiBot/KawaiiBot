package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.extensions.json
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.MessageEmbed
import org.json.JSONObject
import java.net.URLEncoder

@Command(description = "Find the 'best' definition of whatever word you pass", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Urban : ICommand {
    private fun prepObject(def: JSONObject): MessageEmbed {
        val word = def.getString("word")
        val author = def.getString("author")
        val definition = def.getString("definition")
        val example = def.optString("example") ?: "*No example ;-;*"
        val thumbsUp = def.getInt("thumbs_up")
        val thumbsDown = def.getInt("thumbs_down")

        return EmbedBuilder().run {
            setColor(KawaiiBot.embedColor)
            setDescription("**$word**\n*by $author*")
            addField("Definition", definition, false)
            if (example.length <= 1024 && example.isNotEmpty()) {
                addField("Example", example, false)
            }
            setFooter("\uD83D\uDC4D $thumbsUp | \uD83D\uDC4E $thumbsDown", null)
            build()
        }
    }

    override fun run(ctx: CommandContext) {
        RequestUtil
                .get("http://api.urbandictionary.com/v0/define?term=${URLEncoder.encode(ctx.argString, "utf-8")}")
                .thenAccept {
                    val defs = it.json()?.getJSONArray("list")
                            ?: return@thenAccept ctx.send("I think something broke ;-;")
                    if (defs.length() == 0) {
                        return@thenAccept ctx.send("Couldn't find your search in the dictionary...")
                    }
                    var best = defs.getJSONObject(0)
                    var maxThumbs = best.getInt("thumbs_up")
                    for (i in 1..(defs.length() - 1)) {
                        val def = defs.getJSONObject(i)
                        val thumbsUp = def.getInt("thumbs_up")
                        val word = def.getString("word")
                        if (thumbsUp > maxThumbs && word.equals(ctx.argString, true)) {
                            best = def
                            maxThumbs = thumbsUp
                        }
                    }
                    if (best.getString("definition").length > 1024) {
                        val permalink = best.getString("permalink")
                        return@thenAccept ctx.send("The definition is too big for Discord, check it out here $permalink")
                    }
                    val embed = prepObject(best)
                    ctx.sendEmbed(embed)
                }
                .thenException {
                    KawaiiBot.LOG
                            .error("urban api call failed", it)
                    ctx.send("There was an error processing the call to Urban Dictionary ;-;")
                }
    }
}
