package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.devoxin.flight.Context
import me.devoxin.flight.models.Cog
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.arguments.Name
import me.alexflipnote.kawaiibot.utils.Helpers

import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.entities.Member
import me.alexflipnote.kawaiibot.entities.Responses
import net.dv8tion.jda.core.JDA
import java.text.DecimalFormat
import kotlin.math.roundToInt

class Misc : Cog {

    @Command(description = "About me~")
    fun about(ctx: Context) {
        ctx.embed {
            setTitle("ℹ KawaiiBot v${KawaiiBot.version}")
            addField("Developers", "AlexFlipnote, Kromatic, Yvan & Aurieh", false)
            addField("Library", "JDA ${JDAInfo.VERSION}", true)
            addField("My Server!", "https://discord.gg/wGwgWJW", true)
            setThumbnail(ctx.jda.selfUser.effectiveAvatarUrl)
        }
    }

    @Command(description = "Posts a link to the official Patreon", aliases = ["support", "patreon"])
    fun donate(ctx: Context){
        ctx.send("You can support me here \uD83C\uDF80\n**<https://www.patreon.com/KawaiiBot>**")
    }

    @Command(description = "Pong!")
    fun ping(ctx: Context) {
        val start = System.currentTimeMillis()

        ctx.send("BEEP!") { m ->
            val end = System.currentTimeMillis()
            m.editMessage("WS: ${ctx.jda.ping}ms | REST: ${end - start}ms").queue()
        }
    }

    @Command(description = "View internal information")
    fun stats(ctx: Context) {
        val dpFormatter = DecimalFormat("0.00")
        val uptime = Helpers.parseTime(KawaiiBot.uptime())
        val ramUsedRaw = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val ramUsedMB = ramUsedRaw / 1048576
        val ramUsedPercent = dpFormatter.format(ramUsedRaw.toDouble() / Runtime.getRuntime().totalMemory() * 100)
        val shardCount = KawaiiBot.shardManager.shardsTotal
        val averageShardLatency = KawaiiBot.shardManager.averagePing.roundToInt()
        val deadShards = KawaiiBot.shardManager.shards.filterNot { shard -> shard.status == JDA.Status.CONNECTED }
        val onlineShards = KawaiiBot.shardManager.shardsTotal - deadShards.size

        val secondsSinceBoot = (KawaiiBot.uptime() / 1000).toDouble()
        val commandsPerSecond = (KawaiiBot.pornUsage + KawaiiBot.otherCommandUsage) / secondsSinceBoot
        val formattedCPS = dpFormatter.format(commandsPerSecond)

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
        sb.append("• Normal Cmds   :: ${KawaiiBot.otherCommandUsage}\n")
        sb.append("  • Per Second  :: $formattedCPS\n\n")
        sb.append("• Shards Online :: $onlineShards/$shardCount\n")
        if (deadShards.isNotEmpty()) {
            sb.append("  - ${deadShards.map { it.shardInfo.shardId }.sorted().joinToString(" ")}\n")
        }
        sb.append("• Average Ping  :: ${averageShardLatency}ms\n")
        sb.append("```")
        ctx.send(sb.toString())
    }

    override fun name(): String {
        return "Misc"
    }
}