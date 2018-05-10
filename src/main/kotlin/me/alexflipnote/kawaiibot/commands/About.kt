package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.Permission

@Command(description = "About me~", botPermissions = [Permission.MESSAGE_EMBED_LINKS])
class About : ICommand {
    override fun run(ctx: CommandContext) {
        ctx.channel.sendMessage(EmbedBuilder()
                .setColor(KawaiiBot.embedColor)
                .setTitle("ℹ KawaiiBot v${KawaiiBot.version}")
                .addField("Developers", "AlexFlipnote, Kromatic, Yvan & Aurieh", false)
                .addField("Library", "JDA ${JDAInfo.VERSION}", true)
                .addField("My Server!", "https://discord.gg/wGwgWJW", true)
                .setThumbnail(ctx.jda.selfUser.effectiveAvatarUrl)
                .build()
        ).queue()
    }
}
