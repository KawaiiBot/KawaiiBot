package me.alexflipnote.kawaiibot.commands

import java.lang.StringBuilder
import java.text.DecimalFormat
import kotlin.math.roundToInt
import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.JDA

@Command(description = "Change current playing", developerOnly = true)
class SetPlaying : ICommand {
    override fun run(ctx: CommandContext) {
      val set_status = ctx.args.asDisplayString
      KawaiiBot.shardManager.setGame(Game.playing(set_status))
      ctx.send("I have set playing status to `${set_status}`")
    }
}
