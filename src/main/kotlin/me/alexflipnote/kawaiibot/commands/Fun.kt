package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.entities.Cog

class Fun : Cog {
    private val rpsOptions = listOf("rock", "paper", "scissors")

    @Command(description = "Rock, paper, scissors!")
    fun rps(ctx: Context, choice: String) {
        if (!rpsOptions.contains(choice)) {
            return ctx.send("You have to pick between ${Helpers.monospaced(rpsOptions)} ;-;")
        }

        val selected = rpsOptions.random()

        when (choice) {
            selected -> return ctx.send("It was a tie, no one wins...")
            "rock" -> {
                if (selected == "paper") {
                    ctx.send("You lose! \uD83D\uDCC4\n**$selected** covers **$choice**")
                } else {
                    ctx.send("You win! ✊\n**$choice** smashes **$selected**")
                }
            }
            "paper" -> {
                if (selected == "scissors") {
                    ctx.send("You lose! ✂\n**$selected** cut **$choice**")
                } else {
                    ctx.send("You win! \uD83D\uDCC4\n**$choice** covers **$selected**")
                }
            }
            "scissors" -> {
                if (selected == "rock") {
                    ctx.send("You lose! ✊\n**$selected** smashes **$choice**")
                } else {
                    ctx.send("You win! ✂\n**$choice** cut **$selected**")
                }
            }
        }
    }
}
