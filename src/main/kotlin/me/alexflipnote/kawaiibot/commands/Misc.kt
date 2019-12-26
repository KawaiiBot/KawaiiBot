package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.utils.Helpers
import me.devoxin.flight.annotations.Command
import me.devoxin.flight.api.Context
import me.devoxin.flight.arguments.Greedy
import me.devoxin.flight.models.Cog
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.entities.User
import java.text.DecimalFormat
import kotlin.math.roundToInt

class Misc : Cog {
    private val dpFormatter = DecimalFormat("0.00")

    @Command(description = "About me~")
    fun about(ctx: Context) {
        ctx.send {
            setTitle("ℹ KawaiiBot v${KawaiiBot.VERSION}")
            addField("Developers", "AlexFlipnote, devoxin, Yvan, Aurieh, stupid cat & william", false)
            addField("Library", "JDA ${JDAInfo.VERSION}", true)
            addField("My Server!", "https://discord.gg/wGwgWJW", true)
            setThumbnail(ctx.jda.selfUser.effectiveAvatarUrl)
        }
    }

    @Command(description = "Posts a link to the official Patreon", aliases = ["support", "patreon"])
    fun donate(ctx: Context) {
        ctx.send("You can support me here \uD83C\uDF80\n**<https://www.patreon.com/KawaiiBot>**")
    }

    @Command(description = "Pong!")
    fun ping(ctx: Context) {
        ctx.jda.restPing.queue {
            ctx.send("WS: ${ctx.jda.gatewayPing}ms | REST: ${it}ms")
        }
    }

    @Command(description = "Displays your, or another user's avatar")
    fun avatar(ctx: Context, @Greedy user: User = ctx.author) {
        ctx.send("${user.name}'s Avatar\n${user.effectiveAvatarUrl}?size=1024")
    }

    @Command(description = "View internal information")
    fun stats(ctx: Context) {
        val uptime = Helpers.parseTime(KawaiiBot.uptime)
        val ramUsedRaw = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val ramUsedMB = ramUsedRaw / 1048576
        val ramUsedPercent = dpFormatter.format(ramUsedRaw.toDouble() / Runtime.getRuntime().totalMemory() * 100)
        val shardCount = KawaiiBot.shardManager.shardsTotal
        val averageShardLatency = KawaiiBot.shardManager.averageGatewayPing.roundToInt()
        val deadShards = KawaiiBot.shardManager.shards.filterNot { it.status == JDA.Status.CONNECTED }
        val onlineShards = KawaiiBot.shardManager.shardsTotal - deadShards.size

        val secondsSinceBoot = (KawaiiBot.uptime / 1000).toDouble()
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

    @Command(description = "Invite me to your server :3")
    fun invite(ctx: Context) {
        ctx.send("Invite me with this link \uD83C\uDF80\n<https://discordapp.com/oauth2/authorize?client_id=195244341038546948&scope=bot>")
    }

}
