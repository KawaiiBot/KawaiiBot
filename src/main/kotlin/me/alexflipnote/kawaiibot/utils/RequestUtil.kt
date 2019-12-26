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
        fun submit(): CompletableFuture<Response> {
            val future = CompletableFuture<Response>()

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    KawaiiBot.logger.error("An error occurred during a HTTP request to ${call.request().url()}", e)
                    future.completeExceptionally(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    future.complete(response)
                }
            })

            return future
        }

        suspend fun await(): Response {
            return submit().await()
        }
    }

    fun get(url: String, headers: Headers = Headers.of()): PendingRequest {
        return request {
            url(url)
            headers(headers)
        }
    }

    fun request(builder: Request.Builder.() -> Unit): PendingRequest {
        val request = Request.Builder()
                .header("User-Agent", "KawaiiBot/${KawaiiBot.VERSION} (https://kawaiibot.xyz)")
                .apply(builder)
                .build()

        return PendingRequest(request)
    }

    fun getJson(url: String, headers: Headers = Headers.of()): CompletableFuture<JSONObject> {
        return get(url, headers).submit()
            .thenApply {
                it.json()
                    ?: throw IllegalStateException("Response from $url was not a valid JSON object!\n" +
                        "Response code: ${it.code()}, message: ${it.message()}")
            }
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

fun MediaType.APPLICATION_JSON(): MediaType = okhttp3.MediaType.parse("application/json")!!
