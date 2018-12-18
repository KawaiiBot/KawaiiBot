package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.devoxin.flight.Context
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.arguments.Greedy
import me.devoxin.flight.arguments.Name
import me.devoxin.flight.models.Attachment
import me.devoxin.flight.models.Cog
import net.dv8tion.jda.core.entities.Member

class Action : Cog {

    private val dabComments = arrayOf("Dabs on haters", "Dabbing is so 2016", "#DabIsNotDead")

    private fun sendAction(ctx: Context, target: Member?, type: String, action: String?) {
        KawaiiBot.wolkeApi.getRandomImage(action).queue {
            if (it == null) {
                return@queue ctx.send("I-I couldn't find a $type image... I'm sorry ;-;")
            }

            val formattedAction = if (target != null && action != null)
                String.format(action, target.user.name, ctx.author.name)
            else action

            ctx.embed {
                setDescription(formattedAction)
                setColor(KawaiiBot.embedColor)
                setImage(it.url)
            }
        }
    }

    @Command(description = "Hold the hand of someone :3")
    fun handhold(ctx: Context, @Name("target") @Greedy target: Member) {
        if (target.user.idLong == ctx.jda.selfUser.idLong) {
            return ctx.send("*Holds **${ctx.author.name}**'s hand back*")
        }

        if (target.user.idLong == ctx.author.idLong) {
            return ctx.send("Sorry to see you alone ;-;")
        }

        sendAction(ctx, target, "handholding", "**%s**, **%s** is holding your hand")
    }

    @Command(description = "Give someone a hug o////o")
    fun hug(ctx: Context, @Name("target") @Greedy target: Member) {
        if (target.user.idLong == ctx.jda.selfUser.idLong) {
            return ctx.send("*Hugs **${ctx.author.name}** back* ❤")
        }

        if (target.user.idLong == ctx.author.idLong) {
            return ctx.send("Sorry to see you alone...")
        }

        sendAction(ctx, target, "hug", "**%s**, you got a hug from **%s**")
    }

    @Command(description = "Call someone a baka")
    fun baka(ctx: Context, @Name("target") @Greedy target: Member) {
        if (target.user.idLong == ctx.jda.selfUser.idLong) {
            return ctx.send("**${ctx.author.asMention}** how could you :'(")
        }

        if (target.user.idLong == ctx.author.idLong) {
            return ctx.upload(
                    Attachment.from(Helpers.getImageStream("images/selfbaka.jpg"), "selfbaka.jpg")
            )
        }

        sendAction(ctx, target, "baka", "**%s**, **%s** called you a baka")
    }

    @Command(description = "Posts a crying picture when you're sad ;-;")
    fun cry(ctx: Context) = sendAction(ctx, null, "cry", null)

    @Command(description = "Posts a blushing picture when you just can't hold it >w<")
    fun blush(ctx: Context) = sendAction(ctx, null, "blush", null)

    @Command(description = "Dab on haters")
    fun dab(ctx: Context) {
        val comment = Helpers.chooseRandom(dabComments)
        sendAction(ctx, null, "dab", "**$comment**")
    }

    @Command(description = "Posts a dancing image, get down and boogie")
    fun dance(ctx: Context) = sendAction(ctx, null, "dance", null)

    @Command(description = "Displays a random discord meme")
    fun discordmeme(ctx: Context) = sendAction(ctx, null, "discord_memes", null)

}
