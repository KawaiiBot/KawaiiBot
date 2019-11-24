package me.alexflipnote.kawaiibot.utils

import kotlinx.coroutines.future.await
import me.alexflipnote.kawaiibot.KawaiiBot
import okhttp3.Headers
import java.util.concurrent.CompletableFuture

private const val BASE_URL = "https://api.weeb.sh"

class WeebApi(private val token: String) {
    private val defaultHeaders = Headers.of("Authorization", "Wolke $token")

    inner class PendingRequest(private val route: String) {
        fun submit(): CompletableFuture<Image> {
            val future = CompletableFuture<Image>()

            KawaiiBot.httpClient.get(route, defaultHeaders).queue {
                val body = it?.json() ?: return@queue callback(null)
                val status = body.getInt("status")
                val ok = status >= 200 && status <= 400

                if (!ok) {
                    future.completeExceptionally(null) // @todo
                }

                val id = body.getString("id")
                val nsfw = body.getBoolean("nsfw")
                val fileType = body.getString("fileType")
                val url = body.getString("url")

                future.complete(Image(id, nsfw, fileType, url))
            }

            return future
        }

        suspend fun await(): Image? {
            val future = CompletableFuture<Image?>()

            submit {
                future.complete(it)
            }

            return future.await()
        }
    }

    fun getRandomImage(type: String? = null): PendingRequest {
        val route = if (type == null) {
            Routes.RandomImage.compile()
        } else {
            Routes.RandomImageByType.compile(type)
        }
        return PendingRequest(route)
    }

    fun getImageById(id: String): PendingRequest {
        val route = Routes.ImageById.compile(id)
        return PendingRequest(route)
    }

    fun getImageTags(): PendingRequest {
        val route = Routes.ImageTags.compile()
        return PendingRequest(route)
    }

    fun getImageTypes(): PendingRequest {
        val route = Routes.ImageTypes.compile()
        return PendingRequest(route)
    }
}

enum class Routes(private val route: String) {
    ImageById("/images/%s"),
    RandomImage("/images/random"),
    RandomImageByType("/images/random?type=%s"),
    ImageTypes("/images/types"),
    ImageTags("/images/tags");

    fun compile(vararg parameters: String): String {
        return String.format(BASE_URL + route, *parameters)
    }
}

class Image(
    val id: String,
    val nsfw: Boolean,
    val fileType: String,
    val url: String
)
