package me.alexflipnote.kawaiibot.commands

import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Posts a link to the official Patreon", aliases = ["patreon", "donate"])
class Support : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.send("You can support me here \uD83C\uDF80\n**<https://github.com/sponsors/AlexFlipnote>**")
    }
}
