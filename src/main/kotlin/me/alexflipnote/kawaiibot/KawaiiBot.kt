package me.alexflipnote.kawaiibot


import me.alexflipnote.kawaiibot.apis.StatsPoster
import me.alexflipnote.kawaiibot.hooks.CommandClientHook
import me.alexflipnote.kawaiibot.utils.RequestUtil
import me.alexflipnote.kawaiibot.apis.WeebApi
import me.devoxin.flight.api.CommandClient
import me.devoxin.flight.api.CommandClientBuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import org.slf4j.LoggerFactory
import java.awt.Color
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import javax.security.auth.login.LoginException

object KawaiiBot {

    const val VERSION = "4.0.0"
    private val bootTime = System.currentTimeMillis()

    val developerIds = setOf(86477779717066752L, 180093157554388993L, 261912303132344320L, 115076505549144067L)
    val logger = LoggerFactory.getLogger("KawaiiBot")
    val config = Properties()

    lateinit var shardManager: ShardManager
    lateinit var commandHandler: CommandClient
    lateinit var embedColor: Color
    lateinit var wolkeApi: WeebApi
    val httpClient = RequestUtil()

    var otherCommandUsage = 0
    var pornUsage = 0

    val uptime by lazy { System.currentTimeMillis() - bootTime }

    @ExperimentalStdlibApi
    @Throws(LoginException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val production = args.isNotEmpty() && args[0].equals("--sharded", ignoreCase = true)
        if (production) {
            logger.info("Running in production mode!")
            config.load(FileInputStream("config.properties"))
        } else {
            logger.info("Running in development mode!")
            config.load(FileInputStream("dev.properties"))
        }

        val defaultPrefix = config.getProperty("prefix")
        val wolkeApiKey = config.getProperty("wolke")
        embedColor = Color.decode(config.getProperty("color", "0xC29FAF"))

        if (wolkeApiKey != null) {
            logger.info("Wolke API key present, enabling Weeb4J...")
            wolkeApi = WeebApi(wolkeApiKey)
        }

        commandHandler = CommandClientBuilder()
            .setPrefixes(defaultPrefix)
            .setAllowMentionPrefix(true)
            .setIgnoreBots(true)
            .setOwnerIds(*developerIds.toLongArray())
            .addEventListeners(CommandClientHook())
            .registerDefaultParsers()
            .build()

        commandHandler.registerCommands("me.alexflipnote.kawaiibot.commands")

        shardManager = DefaultShardManagerBuilder()
                .setShardsTotal(-1)
                .setToken(config.getProperty("token"))
                .setActivity(Activity.playing("${defaultPrefix}help"))
                .addEventListeners(commandHandler)
                .build()

        shardManager.retrieveApplicationInfo().queue {
            logger.info("Bot info: ${it.name} (${it.id})")

            if (production) {
                StatsPoster(it.id)
            }
        }
    }
}
