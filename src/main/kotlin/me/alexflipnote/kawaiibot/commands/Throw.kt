package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.ThrowResource
import me.alexflipnote.kawaiibot.extensions.clean
import me.alexflipnote.kawaiibot.utils.Helpers
import me.alexflipnote.kawaiibot.utils.ResourceUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Throw something at someone >:3", guildOnly = true)
class Throw : ICommand {
    private val resource = ResourceUtil.readJson<ThrowResource>("responses/throw.json")

    override fun run(ctx: CommandContext) {
        val target = ctx.args.asMember?.effectiveName?.clean() ?: return ctx.send("Who are you expecting to hit with this?")
        val author = ctx.member!!.effectiveName.clean()

        val targetQuote = Helpers.chooseRandom(resource.targetQuotes)
        val authorQuote = Helpers.chooseRandom(resource.authorQuotes)
        val item = Helpers.chooseRandom(resource.items)

        ctx.send(
                "**$author** threw $item at **$target**\n\n" +
                        "$target: $targetQuote\n$author: $authorQuote"
        )
    }
}