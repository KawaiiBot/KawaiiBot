package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.alexflipnote.kawaiibot.utils.ResourceUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import org.apache.commons.text.StringSubstitutor

@Command(description = "Throw a boot at someone >:3")
class Boot : ICommand {
    private val quotes = ResourceUtil.readJson<List<String>>("responses/boot.json")

    override fun run(ctx: CommandContext) {
        val target = ctx.args.nextMemberOrNull() ?: return ctx.send("Give me someone to throw a boot at >-<")
        val values = mapOf("author" to ctx.author.name, "target" to target.user.name)
        val quote = Helpers.chooseRandom(quotes)
        val substitutor = StringSubstitutor(values, "{", "}")
        ctx.send(substitutor.replace(quote))
    }
}