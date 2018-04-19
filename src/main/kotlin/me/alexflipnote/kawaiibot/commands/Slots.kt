package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Roll the slot machine", aliases = ["slot", "bet"])
class Slots : ICommand {
    private val fruits = arrayOf("🍎", "🍊", "🍐", "🍋", "🍉", "🍇", "🍓", "🍒")

    override fun run(ctx: CommandContext) {
        val a = Helpers.chooseRandom(fruits)
        val b = Helpers.chooseRandom(fruits)
        val c = Helpers.chooseRandom(fruits)

        val response = "**${ctx.author.name}** rolled the slots...\n"
        val abc = "**[ $a $b $c ]**\n"

        if (a == b && b == c && c == a) {
            ctx.send(response + abc + "and won! \uD83C\uDF89")
        } else if (a == b || b == c || c == a) {
            ctx.send(response + abc + "and almost won (2/3)")
        } else {
            ctx.send(response + abc + "and lost...")
        }

    }
}
