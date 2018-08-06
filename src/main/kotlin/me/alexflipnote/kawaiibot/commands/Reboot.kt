package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command


@Command(description = "Reboot JDA shards", developerOnly = true)
class Reboot : ICommand {

    override fun run(ctx: CommandContext) {
        val shards: List<String> = ctx.args.bySpace

        if (shards.any { it.toInt() < 0 || it.toInt() >= KawaiiBot.shardManager.shardsTotal }) {
            return ctx.send("Invalid shard IDs were specified, check your query and then try again.")
        }

        for (shardId in shards) {
            KawaiiBot.shardManager.restart(shardId.toInt())
        }
    }

}