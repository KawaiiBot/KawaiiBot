package me.alexflipnote.kawaiibot.utils

import me.alexflipnote.kawaiibot.KawaiiBot
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture

typealias BuilderBlock = Request.Builder.() -> Unit

object RequestUtil {
    val client = OkHttpClient.Builder().build()!!

    fun Call.wrapCallback(): CompletableFuture<Response> {
        val fut = CompletableFuture<Response>()
        val cb = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                fut.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {
                fut.complete(response)
            }
        }
        enqueue(cb)
        return fut
    }

    inline fun request(block: BuilderBlock): CompletableFuture<Response> {
        return client.newCall(Request.Builder().apply(block).build()).wrapCallback()
    }

    inline fun get(url: String, block: BuilderBlock = {}) = request {
        get()
        url(url)
        header("User-Agent", "KawaiiBot/${KawaiiBot.version} (https://kawaiibot.xyz)")
        block()
    }

    inline fun post(url: String, body: RequestBody, block: BuilderBlock = {}) = request {
        post(body)
        url(url)
        header("User-Agent", "KawaiiBot/${KawaiiBot.version} (https://kawaiibot.xyz)")
        block()
    }

    fun <K, V> jsonBody(vararg pairs: Pair<K, V>): RequestBody {
        return RequestBody.create(MediaType.APPLICATION_JSON, JSONObject(mutableMapOf(*pairs)).toString())
    }

    /**
     * An automatically closing request body implementation
     * **NOTE: not all methods auto-close**
     */
    class ClosingResponseBody(private val raw: ResponseBody) : AutoCloseable {
        private var closed = false

        /**
         * Get a string representation of the body
         * This method buffers the entire body in-memory
         * and then closes the stream.
         *
         * @return body as a [kotlin.String]
         */
        fun string() = isValid { raw.string() }

        /**
         * Get a bytestream representation of the body
         * This method does not do any buffering and you
         * have to close the stream on your own.
         *
         * @return body as an [java.io.InputStream]
         */
        fun byteStream() = raw.byteStream()

        /**
         * Get a byte array representation of the body
         * This method buffers the entire body in-memory
         * and then closes the stream.
         *
         * @return body as a [kotlin.ByteArray]
         */
        fun bytes() = isValid { raw.bytes() }

        /**
         * Get a character stream representation of the body
         * This method does not do any buffer and you
         * have to close the stream on your own.
         *
         * @return body as a [java.io.Reader]
         */
        fun charStream() = raw.charStream()

        fun source() = raw.source()

        fun contentLength() = raw.contentLength()

        fun contentType() = raw.contentType()

        override fun close() {
            closed = true
            raw.close()
        }

        private inline fun <T : Any> isValid(calc: () -> T): T {
            if (closed) throw IllegalStateException("already closed")
            use { return calc() }
        }
    }

    object MediaType {
        val APPLICATION_JSON = okhttp3.MediaType.parse("application/json")
    }
}