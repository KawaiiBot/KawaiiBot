package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.devoxin.flight.Context
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.arguments.Greedy
import me.devoxin.flight.arguments.Name
import me.devoxin.flight.arguments.Optional
import me.devoxin.flight.models.Cog
import net.dv8tion.jda.core.JDAInfo
import me.devoxin.flight.parsers.MemberParser
import net.dv8tion.jda.core.entities.Member
import me.alexflipnote.kawaiibot.utils.WeebApi
import net.dv8tion.jda.core.MessageBuilder

class Action : Cog {

    val api = KawaiiBot.wolkeApi

    @Command(description = "Hold the hand of someone :3")
    fun handhold(ctx: Context, @Name("target") target: Member?) {
        val image = api.getRandomImage("handholding")
        if (target?.user?.idLong == null) {
            ctx.send("Are you trying to hold the hand of the ghost...?")
        }
        if (target?.user?.idLong == ctx.jda.selfUser.idLong) {
            ctx.send("*Holds **${ctx.author.name}**'s hand back*")
        }
        if (target?.user?.idLong == ctx.author.idLong) {
            ctx.send("Sorry to see you alone ;-;")
        } else {
            api.getRandomImage("handholding").queue { image ->
                ctx.embed {
                    setDescription("**${target?.user?.name}**, **${ctx.author.name}** is holding your hand")
                    setColor(KawaiiBot.embedColor)
                    setImage(image?.url)
                }
            }
        }
    }

    @Command(description = "Give someone a hug o////o")
    fun hug(ctx: Context, @Name("target") target: Member?) {
        if (target?.user?.idLong == null) {
            ctx.send("Are you trying to hug thin air...?")
        }
        if (target?.user?.idLong == ctx.jda.selfUser.idLong) {
            ctx.send("*Hugs **${ctx.author.name}** back* ❤")
        }
        if (target?.user?.idLong == ctx.author.idLong) {
            ctx.send("Sorry to see you alone...")
        } else {
            api.getRandomImage("hug").queue { image ->
                ctx.embed {
                    setDescription("**${target?.effectiveName}**, you got a hug from **${ctx.author.name}**")
                    setColor(KawaiiBot.embedColor)
                    setImage(image?.url)
                }
            }
        }
    }

    @Command(description = "Call someone a baka")
    fun baka(ctx: Context, @Name("target") target: Member?) {
        if (target?.user?.idLong == null) {
            ctx.send("Who are you calling a baka...?")
        }
        if (target?.user?.idLong == ctx.jda.selfUser.idLong) {
            ctx.send("**${ctx.author.name}** how could you :'(")
        }
        if (target?.user?.idLong == ctx.author.idLong) {
            // ctx.channel.sendFile(Helpers.getImageStream("images/selfbaka.jpg"), "selfbaka.jpg").queue() Not sure how flights upload system works yet.
        } else {
            api.getRandomImage("baka").queue { image ->
                ctx.embed {
                    setDescription("**${ctx.author.name}**, called **${target?.user?.name}** a baka")
                    setColor(KawaiiBot.embedColor)
                    setImage(image?.url)
                }
            }
        }
    }

    override fun name(): String {
        return "Action"
    }
}