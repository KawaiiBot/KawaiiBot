package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.jsonArray
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.extensions.json
import me.alexflipnote.kawaiibot.utils.Helpers
import me.alexflipnote.kawaiibot.utils.NSFWCheck
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import java.net.URLEncoder

@Command(description = "Searches e621 and pulls a random image", isNSFW = true)
class E621 : ICommand {
    private val e621Search = "https://e621.net/posts.json?limit=30&tags="

    override fun run(ctx: CommandContext) {
        val args = ctx.args.bySpace
        if (!NSFWCheck.check(args)) {
            return ctx.send("This search term is considered illegal, therefor I won't continue with your request...")
        }
        RequestUtil.get(e621Search + URLEncoder.encode(args.joinToString(" "), "utf-8")).thenAccept {
            val res = it.json()!!.getJSONArray("posts")
            val random = Helpers.chooseRandom(res)
            val get_image = random.getJSONObject("file").getString("url")
            ctx.send(get_image)
        }.thenException { ctx.send("No results found ;-;") }
    }
}
