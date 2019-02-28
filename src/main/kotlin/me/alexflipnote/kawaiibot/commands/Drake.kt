package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission
import okhttp3.RequestBody

@Command(description = "Make a dank, drake meme", guildOnly = true, botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Drake : AbstractAPICommand() {
    override val path = "/drake"

    override fun makeArgument(ctx: CommandContext): String {
        val args = ctx.argString.split(" | ")
        if (args.size != 2) {
            ctx.send("You need to specify top and body text (split using `|`) ;-;")
            return ""
        }

        return "top=${args[0]}&bottom=${args[1]}"
    }
}
