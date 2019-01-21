package me.alexflipnote.kawaiibot.utils

import kotlinx.coroutines.future.await
import me.alexflipnote.kawaiibot.KawaiiBot
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture

class RequestUtil {
    private val httpClient = OkHttpClient()

    inner class PendingRequest(private val request: Request) {

        fun queue(success: (Response?) -> Unit) {
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    KawaiiBot.logger.error("An error occurred during a HTTP request to ${call.request().url()}", e)
                    success(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    success(response)
                }
            })
        }

        suspend fun await(): Response? {
            val future = CompletableFuture<Response?>()

            queue {
                future.complete(it)
            }

            return future.await()
        }

    }

    public fun get(url: String, headers: Headers = Headers.of()): PendingRequest {
        return request {
            url(url)
            headers(headers)
        }
    }

    public fun post(url: String, body: RequestBody, headers: Headers): PendingRequest {
        return request {
            url(url)
            headers(headers)
            post(body)
        }
    }

    public fun request(builder: Request.Builder.() -> Unit): PendingRequest {
        val request = Request.Builder()
                .header("User-Agent", "KawaiiBot/${KawaiiBot.KAWAIIBOT_VERSION} (https://kawaiibot.xyz)")
                .apply(builder)
                .build()

        return PendingRequest(request)
    }
}

fun Response.json(): JSONObject? {
    val body = body()

    body().use {
        return if (isSuccessful && body != null) {
            JSONObject(body()!!.string())
        } else {
            null
        }
    }
}

public fun createHeaders(vararg kv: Pair<String, String>): Headers {
    val builder = Headers.Builder()

    for (header in kv) {
        builder.add(header.first, header.second)
    }

    return builder.build()
}

fun MediaType.APPLICATION_JSON(): MediaType = okhttp3.MediaType.parse("application/json")!!
