package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.devoxin.flight.Context
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.arguments.Name
import me.devoxin.flight.models.Cog
import net.dv8tion.jda.core.JDAInfo
import me.devoxin.flight.parsers.MemberParser
import net.dv8tion.jda.core.entities.Member

class Fun : Cog {

    @Command(description = "Rock, paper, scissors!")
    fun rps(ctx: Context, @Name("option") option: String) {
        val options = arrayOf("rock", "paper", "scissors")
        val choice = option
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


    override fun name(): String {
        return "Fun"
    }
}