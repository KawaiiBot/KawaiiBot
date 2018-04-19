package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

// shit meme
@Command(description = ":emofdr:", hidden = true, botPermissions = [Permission.MESSAGE_EXT_EMOJI])
class Emofdr : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.send("<:emofdr:353499458924707852>")
    }
}
