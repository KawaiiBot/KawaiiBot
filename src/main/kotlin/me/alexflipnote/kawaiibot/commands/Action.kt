package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.devoxin.flight.Context
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.arguments.Name
import me.devoxin.flight.models.Cog
import net.dv8tion.jda.core.JDAInfo
import me.devoxin.flight.parsers.MemberParser
import net.dv8tion.jda.core.entities.Member

class Action : Cog {

    @Command(description = "Hold the hand of someone :3")
    fun handhold(ctx: Context, @Name("target") target: Member) {
        if (target.user.idLong == null) {
            ctx.send("Are you trying to hold the hand of the ghost...?")
        }
        if (target.user.idLong == ctx.jda.selfUser.idLong) {
            ctx.send("*Holds **${ctx.author.name}**'s hand back*")
        }
        if (target.user.idLong == ctx.author.idLong) {
            ctx.send("Sorry to see you alone ;-;")
        } else {
            ctx.send("test")
        }
    }

    override fun name(): String {
        return "Action"
    }
}