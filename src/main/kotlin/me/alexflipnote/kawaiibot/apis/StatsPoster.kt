package me.alexflipnote.kawaiibot.apis

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.apis.botlists.*
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class StatsPoster(private val botId: String) {
    private val task = Executors.newSingleThreadScheduledExecutor()

    private val lists = listOf(
        BotsGG(botId, loadKeyFromConfig("botsgg")),
        Carbonitex(loadKeyFromConfig("carbonitex")),
        DiscordBoats(botId, loadKeyFromConfig("discordboats")),
        DiscordBotList(botId, loadKeyFromConfig("dbl")),
        DivineDiscordBots(botId, loadKeyFromConfig("divine"))
    )

    init {
        task.scheduleAtFixedRate(::postStats, 0, 1, TimeUnit.DAYS)
    }

    fun postStats() {
        log.info("Running scheduled postStats task (Updating statistics)")

        for (list in lists) {
            list.postStats()
        }
    }

    private fun loadKeyFromConfig(name: String): String {
        return KawaiiBot.config.getProperty("list-$name")
            ?: throw IllegalArgumentException("A config value for $name does not exist!")
    }

    companion object {
        private val log = LoggerFactory.getLogger(StatsPoster::class.java)
    }
}
