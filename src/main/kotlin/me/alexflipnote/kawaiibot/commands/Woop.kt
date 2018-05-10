package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Woop woop!", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Woop : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.channel.sendFile(Helpers.getImageStream("images/woop.gif"), "woop.gif").queue()
    }
}
