package me.alexflipnote.kawaiibot.apis.botlists

import me.alexflipnote.kawaiibot.KawaiiBot
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class Carbonitex(key: String) : BotList(
    "Carbonitex",
    "https://www.carbonitex.net/discord/data/botdata.php",
    key,
    "servercount"
) {
    override fun buildRequest(): Request {
        val payload = buildPayload()
        val body = RequestBody.create(payload.contentType, payload.body)

        return Request.Builder()
            .url(this.postUrl)
            .post(body)
            .build()
    }

    override fun buildPayload(): Payload {
        val content = JSONObject()
            .put("key", authorization)
            .put(countKey, KawaiiBot.shardManager.guildCache.size())

        return Payload(
            applicationJson,
            content.toString()
        )
    }
}
