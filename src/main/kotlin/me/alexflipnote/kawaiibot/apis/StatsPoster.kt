package me.alexflipnote.kawaiibot.apis

import me.alexflipnote.kawaiibot.apis.botlists.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class StatsPoster(private val botId: String) {
    // TODO: Read keys from config
    private val task = Executors.newSingleThreadScheduledExecutor()

    private val lists = listOf(
        BotsGG(botId, ""),
        Carbonitex(""),
        DiscordBoats(botId, ""),
        DiscordBotList(botId, ""),
        DivineDiscordBots(botId, "")
    )

    init {
        task.schedule(::postStats, 1, TimeUnit.DAYS)
    }

    fun postStats() {
        for (list in lists) {
            list.postStats()
        }
    }
}
