package me.alexflipnote.kawaiibot.commands

import com.github.natanbc.weeb4j.image.NsfwFilter
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Posts a dancing image, get down and boogie", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class Dance : ICommand {

    override fun run(ctx: CommandContext) {
        val api = KawaiiBot.wolkeApi
        api.getRandomImage("dance", null, null, NsfwFilter.NO_NSFW, null).async { image ->
            ctx.sendEmbed {
                setImage(image.url)
            }
        }
    }

}