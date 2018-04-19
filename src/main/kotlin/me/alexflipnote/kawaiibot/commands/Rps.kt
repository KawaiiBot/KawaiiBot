package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Rock, paper, scissors!")
class Rps : ICommand {

    private val options = arrayOf("rock", "paper", "scissors")

    override fun run(ctx: CommandContext) {

        val choice = ctx.argString

        if (choice.isEmpty() || !options.contains(choice))
            return ctx.send("You have to pick between `rock`, `paper` or `scissors` ;-;")

        val selected = Helpers.chooseRandom(options)

        if (choice == selected)
            return ctx.send("It was a tie, no one wins...")

        if (choice == "rock") {
            if (selected == "paper")
                ctx.send("You lose! \uD83D\uDCC4\n**$selected** covers **$choice**")
            else
                ctx.send("You win! ✊\n**$choice** smashes **$selected**")
        } else if (choice == "paper") {
            if (selected == "scissors")
                ctx.send("You lose! ✂\n**$selected** cut **$choice**")
            else
                ctx.send("You win! \uD83D\uDCC4\n**$choice** covers **$selected**")
        } else if (choice == "scissors") {
            if (selected == "rock")
                ctx.send("You lose! ✊\n**$selected** smashes **$choice**")
            else
                ctx.send("You win! ✂\n**$choice** cut **$selected**")
        }

    }

}