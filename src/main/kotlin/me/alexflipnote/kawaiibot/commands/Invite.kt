package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Invite me to your server :3")
class Invite : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.send("Invite me with this link \uD83C\uDF80\n<https://discordapp.com/oauth2/authorize?client_id=195244341038546948&scope=bot>")
    }
}
