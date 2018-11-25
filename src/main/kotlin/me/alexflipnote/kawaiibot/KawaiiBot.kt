package me.alexflipnote.kawaiibot

import com.github.natanbc.weeb4j.TokenType
import com.github.natanbc.weeb4j.Weeb4J
import me.devoxin.flight.CommandClient
import me.devoxin.flight.CommandClientBuilder
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.bot.sharding.ShardManager
import net.dv8tion.jda.core.entities.Game
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import java.awt.Color
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import javax.security.auth.login.LoginException

object KawaiiBot {

    const val version = "3.3.0"
    private val bootTime = System.currentTimeMillis()
    val developerIds = setOf(86477779717066752L, 180093157554388993L, 261912303132344320L, 115076505549144067L)
    val logger = LoggerFactory.getLogger("KawaiiBot")

    val config = Properties()

    lateinit var shardManager: ShardManager
    lateinit var commandHandler: CommandClient
    lateinit var httpClient: OkHttpClient
    lateinit var wolkeApi: Weeb4J
    lateinit var embedColor: Color

    var otherCommandUsage = 0
    var pornUsage = 0

    @Throws(LoginException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty() && args[0].equals("--sharded", ignoreCase = true)) {
            logger.info("Running in production mode!")
            config.load(FileInputStream("config.properties"))
        } else {
            logger.info("Running in development mode!")
            config.load(FileInputStream("dev.properties"))
        }

        val defaultPrefix = config.getProperty("prefix")
        val wolkeApiKey = config.getProperty("wolke")
        embedColor = Color.decode(config.getProperty("color", "0xC29FAF"))

        // TODO: Consider dumping weeb4j in favour of our own wrapper.
        if (wolkeApiKey != null) {
            logger.info("Wolke API key present, enabling Weeb4J...")
            wolkeApi = Weeb4J.Builder()
                    .setToken(TokenType.WOLKE, wolkeApiKey)
                    .setUserAgent("KawaiiBot/$version (https://kawaiibot.xyz)")
                    .build()
        }

        httpClient = OkHttpClient.Builder().build()

        commandHandler = CommandClientBuilder()
                .setPrefixes(defaultPrefix)
                .setAllowMentionPrefix(true)
                .setIgnoreBots(true)
                .useDefaultHelpCommand(true)
                //.addEventListeners(null) // TODO
                .registerDefaultParsers()
                .build()

        commandHandler.registerCommands("me.alexflipnote.kawaiibot.commands")

        shardManager = DefaultShardManagerBuilder()
                .setShardsTotal(-1)
                .setToken(config.getProperty("token"))
                .setGame(Game.playing("${defaultPrefix}help"))
                .addEventListeners(commandHandler)
                .build()
    }

    fun uptime(): Long {
        return System.currentTimeMillis() - bootTime
    }
}
