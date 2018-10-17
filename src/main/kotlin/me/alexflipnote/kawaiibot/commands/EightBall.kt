package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Consult 8ball to receive an answer", aliases = ["8ball"])
class EightBall : ICommand {
    private val responses = arrayOf("Yes", "No", "Take a wild guess...", "Very doubtful", "Sure", "Without a doubt", "senpai, pls no ;-;", "Most likely", "Might be possible", "You'll be the judge", "no... (╯°□°）╯︵ ┻━┻", "no... baka")

    override fun run(ctx: CommandContext) {
        val responseR = Helpers.chooseRandom(responses)

        if (ctx.argString.isEmpty()) {
            ctx.send("Give me a question please")
        } else {
            ctx.send("\uD83C\uDFB1 **Question:** ${ctx.args.asDisplayString}\n**Answer:** $responseR")
        }

    }
}
