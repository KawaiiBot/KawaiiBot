package me.alexflipnote.kawaiibot.apis.botlists

import me.alexflipnote.kawaiibot.KawaiiBot
import okhttp3.*
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.io.IOException

abstract class BotList(
    /** The formal name of the bot list **/
    val name: String,
    /** The URL to post stats to **/
    val postUrl: String,
    /** The authorization key to use when posting the stats **/
    val authorization: String,
    /** The JSON key to associate with the server count **/
    val countKey: String
) {

    /**
     * The main function that takes care of everything else, including error handling.
     */
    fun postStats() {
        val request = buildRequest()

        try {
            httpClient.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    log.error("Fatal error occurred while posting statistics to $name", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        log.debug("Successfully posted statistics to $name")
                    } else {
                        val code = response.code()
                        val message = response.message()
                        log.warn("Non-success response code while posting statistics to $name. Code=$code, message=$message")
                    }
                }
            })
        } catch (e: Exception) {
            log.error("An error occurred while posting statistics to $name", e)
        }
    }

    /**
     * Creates the request.
     */
    open fun buildRequest(): Request {
        val payload = buildPayload()
        val body = RequestBody.create(payload.contentType, payload.body)

        return Request.Builder()
            .url(postUrl)
            .post(body)
            .header("Authorization", authorization)
            .build()
    }

    /**
     * Creates the statistics payload to be sent to the bot list.
     * By default, this returns a JSON object (application/json).
     *
     * Invoked by buildRequest() before request.
     */
    open fun buildPayload(): Payload {
        val content = JSONObject()
            .put(countKey, KawaiiBot.shardManager.guildCache.size())

        return Payload(
            applicationJson,
            content.toString()
        )
    }

    inner class Payload(
        val contentType: MediaType,
        val body: String
    )

    companion object {
        private val log = LoggerFactory.getLogger(BotList::class.java)
        private val httpClient = OkHttpClient()

        val applicationJson = MediaType.get("application/json")
    }
}
