package me.alexflipnote.kawaiibot.utils

import kotlinx.coroutines.future.await
import me.alexflipnote.kawaiibot.KawaiiBot
import okhttp3.Headers
import java.util.concurrent.CompletableFuture

private const val BASE_URL = "https://api.weeb.sh"

class WeebApi(token: String) {
    private val defaultHeaders = Headers.of("Authorization", "Wolke $token")

    fun getRandomImage(type: String? = null): WeebRequest {
        val route = if (type == null) {
            Routes.RandomImage.compile()
        } else {
            Routes.RandomImageByType.compile(type)
        }
        return WeebRequest(route)
    }

    fun getImageById(id: String): WeebRequest {
        val route = Routes.ImageById.compile(id)
        return WeebRequest(route)
    }

    fun getImageTags(): WeebRequest {
        val route = Routes.ImageTags.compile()
        return WeebRequest(route)
    }

    fun getImageTypes(): WeebRequest {
        val route = Routes.ImageTypes.compile()
        return WeebRequest(route)
    }

    inner class WeebRequest(private val route: String) {
        fun submit(): CompletableFuture<Image> {
            return KawaiiBot.httpClient.get(route, defaultHeaders)
                .submit()
                .thenApply { it.json() ?: throw IllegalStateException("ResponseBody was not a JSON object!") }
                .thenApply {
                    val status = it.getInt("status")

                    if (status !in 200..300) {
                        throw IllegalStateException("Invalid response status code $status")
                    }

                    val id = it.getString("id")
                    val nsfw = it.getBoolean("nsfw")
                    val fileType = it.getString("fileType")
                    val url = it.getString("url")

                    return@thenApply Image(id, nsfw, fileType, url)
                }
        }

        suspend fun await(): Image {
            return submit().await()
        }
    }

    inner class Image(
        val id: String,
        val nsfw: Boolean,
        val fileType: String,
        val url: String
    )
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
