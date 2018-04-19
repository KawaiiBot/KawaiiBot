package me.aurieh.ichigo.extensions

import java.util.concurrent.CompletableFuture

inline fun<T : Any?> futureOf(block: CompletableFuture<T>.() -> Unit): CompletableFuture<T> {
    val fut = CompletableFuture<T>()
    fut.block()
    return fut
}
