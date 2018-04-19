package me.alexflipnote.kawaiibot.commands


import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission

@Command(description = "Dab on haters", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Dab : ICommand {
    private val comments = arrayOf("Dabs on haters", "Dabbing is so 2016", "#DabIsNotDead")

    override fun run(ctx: CommandContext) {
        val comment = Helpers.chooseRandom(comments)

        val api = KawaiiBot.wolkeApi
        api.getRandomImage("dab", null, null, NsfwFilter.NO_NSFW, null).async { image ->
            ctx.channel.sendMessage(EmbedBuilder()
                    .setColor(KawaiiBot.embedColor)
                    .setDescription("**$comment**")
                    .setImage(image.url)
                    .build()
            ).queue()
        }
    }
}
