package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.extensions.jsonArray
import me.alexflipnote.kawaiibot.extensions.thenException
import me.alexflipnote.kawaiibot.utils.Helpers
import me.alexflipnote.kawaiibot.utils.NSFWCheck
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import java.net.URLEncoder

@Command(description = "Searches rule34 and pulls a random image", aliases = ["r34"], isNSFW = true)
class Rule34 : ICommand {
    private val rule34Search = "https://rule34.xxx/index.php?page=dapi&json=1&s=post&q=index&limit=30&tags="
    private val rule34Cdn = "https://img.rule34.xxx/images/"

    override fun run(ctx: CommandContext) {
        val args = ctx.args.bySpace
        if (!NSFWCheck.check(args)) {
            return ctx.send("This search term is considered illegal, therefor I won't continue with your request...")
        }
        RequestUtil.get(rule34Search + URLEncoder.encode(args.joinToString(" "), "utf-8")).thenAccept {
            val random = Helpers.chooseRandom(NSFWCheck.filterJSON(it.jsonArray()!!))
            val imageUrl = rule34Cdn + random.getString("directory") + "/" + random.getString("image")

            ctx.send(imageUrl)
        }.thenException { ctx.send("No results found ;-;") }
    }
}
