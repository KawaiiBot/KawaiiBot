package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.alexflipnote.kawaiibot.utils.ResourceUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Picks from a list of choices")
class Choose : ICommand {
    private val responses = ResourceUtil.readJson<Array<String>>("responses/choose.json")

    override fun run(ctx: CommandContext) {
        val choices = ctx.args.asDisplayString.split("|").map { it.trim() }.filter { it.isNotEmpty() }
        if (choices.isEmpty()) {
            return ctx.send("It looks you didn't give me any choices... (You can split your choices with `|`)")
        }
        val choice = Helpers.chooseRandom(choices)
        val response = Helpers.chooseRandom(responses)
        // TODO: Increase chances for keywords
        ctx.send("**${ctx.author.name}**, ${response.format(choice)}")
    }
}
