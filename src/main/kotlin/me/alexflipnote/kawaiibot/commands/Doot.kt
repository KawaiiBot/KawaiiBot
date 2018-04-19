package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Doot doot doot", botPermissions = [Permission.MESSAGE_ATTACH_FILES], hidden = true)
class Doot : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.channel.sendFile(Helpers.getImageStream("images/doot.gif"), "doot.gif").queue()
    }
}
