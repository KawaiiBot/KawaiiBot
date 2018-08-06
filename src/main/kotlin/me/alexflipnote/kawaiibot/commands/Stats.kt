package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.JDA
import java.lang.StringBuilder
import java.text.DecimalFormat
import kotlin.math.roundToInt

@Command(description = "View internal information", developerOnly = true)
class Stats : ICommand {
    private val dpFormatter = DecimalFormat("0.00")

    override fun run(ctx: CommandContext) {
        val uptime = Helpers.parseTime(KawaiiBot.uptime)
        val ramUsedRaw = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val ramUsedMB = ramUsedRaw / 1048576
        val ramUsedPercent = dpFormatter.format(ramUsedRaw.toDouble() / Runtime.getRuntime().totalMemory() * 100)
        val shardCount = KawaiiBot.shardManager.shardsTotal
        val averageShardLatency = KawaiiBot.shardManager.averagePing.roundToInt()
        val deadShards = KawaiiBot.shardManager.shards.filterNot { shard -> shard.status == JDA.Status.CONNECTED }
        val onlineShards = KawaiiBot.shardManager.shardsTotal - deadShards.size

        val sb = StringBuilder()
        sb.append("```prolog\n")
        sb.append("=== KawaiiBot Statistics ===")
        sb.append("\n\nPROCESS STATISTICS\n")
        sb.append("• Uptime        :: $uptime\n")
        sb.append("• RAM Usage     :: ${ramUsedMB}MB ($ramUsedPercent%)\n")
        sb.append("• Threads       :: ${Thread.activeCount()}")
        sb.append("\n\nBOT STATISTICS\n")
        sb.append("• Guilds        :: ${KawaiiBot.shardManager.guildCache.size()}\n")
        sb.append("• Users         :: ${KawaiiBot.shardManager.userCache.size()}\n")
        sb.append("• Porn Cmds     :: ${KawaiiBot.pornUsage}\n")
        sb.append("• Normal Cmds   :: ${KawaiiBot.otherCommandUsage}\n\n")
        sb.append("• Shards Online :: $onlineShards/$shardCount\n")

        if (deadShards.isNotEmpty()) {
            sb.append("  - ${deadShards.map { it.shardInfo.shardId }.sorted().joinToString(" ")}\n")
        }

        sb.append("• Average Ping  :: ${averageShardLatency}ms\n")
        sb.append("```")

        ctx.send(sb.toString())
    }
}