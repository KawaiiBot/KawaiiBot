package me.alexflipnote.kawaiibot.extensions

import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

private object CompletableFutureHelper {
    val timer = Timer()
}

inline fun <T> CompletableFuture<T>.thenException(crossinline block: (Throwable) -> Unit) {
    exceptionally { block(it); null }
}

fun <T : Any?> CompletableFuture<T>.withTimeout(timeout: Long, interruptIfRunning: Boolean = false, timeUnit: TimeUnit = TimeUnit.SECONDS): CompletableFuture<T> {
    CompletableFutureHelper.timer.schedule(timerTask { this@withTimeout.cancel(interruptIfRunning) }, timeUnit.toMillis(timeout))
    return this
}
