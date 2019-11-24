package me.alexflipnote.kawaiibot.utils

import com.neovisionaries.ws.client.OpeningHandshakeException
import me.alexflipnote.kawaiibot.KawaiiBot
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.impl.JDAImpl
import net.dv8tion.jda.core.exceptions.AccountTypeException
import net.dv8tion.jda.core.requests.Request
import net.dv8tion.jda.core.requests.Response
import net.dv8tion.jda.core.requests.RestAction
import net.dv8tion.jda.core.requests.Route
import net.dv8tion.jda.core.utils.SessionController
import net.dv8tion.jda.core.utils.tuple.Pair
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicLong
import javax.security.auth.login.LoginException

class SpeedyBoi : SessionController {

    private val globalRatelimit = AtomicLong(Long.MIN_VALUE)
    private val sessionManagers = hashMapOf<Int, SessionManager>()

    override fun getGlobalRatelimit() = globalRatelimit.get()
    override fun setGlobalRatelimit(ratelimit: Long) = globalRatelimit.set(ratelimit)

    override fun appendSession(node: SessionController.SessionConnectNode) {
        val managerId = node.shardInfo.shardId % 16
        val manager = sessionManagers.computeIfAbsent(managerId) { SessionManager(it) }
        manager.appendSession(node)
    }

    override fun removeSession(node: SessionController.SessionConnectNode) {
        val managerId = node.shardInfo.shardId % 16
        sessionManagers[managerId]?.removeSession(node)
    }

    override fun getGateway(api: JDA): String {
        val route = Route.Misc.GATEWAY.compile()
        return object : RestAction<String>(api, route) {
            override fun handleResponse(response: Response, request: Request<String>) {
                if (response.isOk) request.onSuccess(response.getObject().getString("url")) else request.onFailure(response)
            }
        }.complete()!!
    }

    override fun getGatewayBot(api: JDA): Pair<String, Int> {
        AccountTypeException.check(api.accountType, AccountType.BOT)
        return object : RestAction<Pair<String, Int>>(api, Route.Misc.GATEWAY_BOT.compile()) {
            override fun handleResponse(response: Response, request: Request<Pair<String, Int>>) {
                try {
                    if (response.isOk) {
                        val `object` = response.getObject()
                        val url = `object`.getString("url")
                        val shards = `object`.getInt("shards")
                        request.onSuccess(Pair.of(url, shards))
                    } else if (response.code == 401) {
                        (api as JDAImpl).verifyToken(true)
                    } else {
                        request.onFailure(LoginException("When verifying the authenticity of the provided token, Discord returned an unknown response:\n" +
                            response.toString()))
                    }
                } catch (e: Exception) {
                    request.onFailure(e)
                }
            }
        }.complete()
    }

    class SessionWorker(
        private val manager: SessionManager,
        private val delay: Long = KawaiiBot.config.getProperty("shardIdentifyDelay", "5000").toLong()
    ) : Thread("Session-Worker-${manager.id}") {

        init {
            super.setUncaughtExceptionHandler { thread, exception -> this.handleFailure(thread, exception) }
        }

        private fun handleFailure(thread: Thread, exception: Throwable) {
            log.error("Worker has failed with throwable!", exception)
        }

        override fun run() {
            try {
                if (delay > 0) {
                    val interval = System.currentTimeMillis() - manager.lastConnect
                    if (interval < delay) {
                        sleep(delay - interval)
                    }
                }
            } catch (ex: InterruptedException) {
                log.error("Unable to backoff", ex)
            }

            processQueue()
            synchronized(manager.lock) {
                manager.worker = null

                if (manager.connectQueue.isNotEmpty()) {
                    manager.runWorker()
                }
            }
        }

        private fun processQueue() {
            var isMultiple = manager.connectQueue.size > 1
            while (manager.connectQueue.isNotEmpty()) {
                val node = manager.connectQueue.poll()
                try {
                    node.run(isMultiple && manager.connectQueue.isEmpty())
                    isMultiple = true
                    manager.lastConnect = System.currentTimeMillis()
                    if (manager.connectQueue.isEmpty())
                        break
                    if (this.delay > 0)
                        sleep(this.delay)
                } catch (e: IllegalStateException) {
                    val t = e.cause
                    if (t is OpeningHandshakeException)
                        log.error("Failed opening handshake, appending to queue. Message: {}", e.message)
                    else
                        log.error("Failed to establish connection for a node, appending to queue", e)
                    manager.appendSession(node)
                } catch (e: InterruptedException) {
                    log.error("Failed to run node", e)
                    manager.appendSession(node)
                    return  // caller should start a new thread
                }
            }
        }
    }

    inner class SessionManager(val id: Int) {
        val connectQueue = ConcurrentLinkedQueue<SessionController.SessionConnectNode>()
        var lastConnect = 0L
        val lock = Object()
        var worker: Thread? = null

        fun appendSession(node: SessionController.SessionConnectNode) {
            connectQueue.add(node)
            runWorker()
        }

        fun removeSession(node: SessionController.SessionConnectNode) {
            connectQueue.remove(node)
        }

        fun runWorker() {
            synchronized(lock) {
                if (worker == null) {
                    worker = SessionWorker(this)
                    worker?.start()
                }
            }
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(SessionController::class.java)!!
    }

}