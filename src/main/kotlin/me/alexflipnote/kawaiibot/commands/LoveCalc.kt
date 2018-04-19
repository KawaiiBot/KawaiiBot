package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.ResourceUtil
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import java.util.*

@Command(description = "Calculate love levels between two members", guildOnly = true, aliases = ["love"])
class LoveCalc : ICommand {
    override fun run(ctx: CommandContext) {
        val first = ctx.args.nextMemberOrNull()?.user ?: return ctx.send("Mention two members, please")
        val second = ctx.args.nextMemberOrNull()?.user ?: return ctx.send("Mention two members, please")
        if (first.id == second.id && first.id == ctx.author.id) {
            val selfhug = ResourceUtil.getResource("images/selfhug.gif")
            ctx.channel.sendMessage("Sorry to see you alone")
                    .addFile(selfhug, "selfhug.gif")
                    .queue()
            return
        } else if (first.id == ctx.jda.selfUser.id || second.id == ctx.jda.selfUser.id) {
            return ctx.send("B-but I don't have feelings :(")
        } else if (first.id == second.id) {
            return ctx.send("I-I'm not sure that's possible...")
        }

        val random = Random()
        random.setSeed(first.idLong + second.idLong)
        val percent = random.nextInt(100)
        val embed = EmbedBuilder().run {
            setTitle("❤ Love Calculator ❤")
            setColor(KawaiiBot.embedColor)
            setDescription("Love between ${first.name} and ${second.name} is at **$percent%**")
            if (percent == 100) {
                appendDescription("\n`Love is in the air\nOh, oh, oh, oh, uh`")
            }
            build()
        }
        ctx.channel.sendMessage(embed).queue()
    }
}