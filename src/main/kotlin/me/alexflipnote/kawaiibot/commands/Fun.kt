package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.Helpers
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.api.Context
import me.devoxin.flight.models.Cog

class Fun : Cog {

    private val rpsOptions = listOf("rock", "paper", "scissors")

    fun ImageAPICommand(ctx: Context, endpoint: String, querystring: String) {
        // TODO: Figure out how Image API works and do stuff here.
    }

    @Command(description = "Rock, paper, scissors!")
    fun rps(ctx: Context, choice: String) {
        if (!rpsOptions.contains(choice)) {
            ctx.send("You have to pick between ${Helpers.monospaced(rpsOptions)} ;-;")
            return
        }

        val selected = rpsOptions.random()

        if (choice == selected) {
            ctx.send("It was a tie, no one wins...")
            return
        } else if (choice == "rock") {
            if (selected == "paper") {
                ctx.send("You lose! \uD83D\uDCC4\n**$selected** covers **$choice**")
            } else {
                ctx.send("You win! ✊\n**$choice** smashes **$selected**")
            }
        } else if (choice == "paper") {
            if (selected == "scissors") {
                ctx.send("You lose! ✂\n**$selected** cut **$choice**")
            } else {
                ctx.send("You win! \uD83D\uDCC4\n**$choice** covers **$selected**")
            }
        } else if (choice == "scissors") {
            if (selected == "rock") {
                ctx.send("You lose! ✊\n**$selected** smashes **$choice**")
            } else {
                ctx.send("You win! ✂\n**$choice** cut **$selected**")
            }
        }
    }

}