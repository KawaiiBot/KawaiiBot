package me.aurieh.ichigo.extensions

import kotlinx.coroutines.experimental.future.await
import net.dv8tion.jda.core.requests.RestAction

suspend fun<T : Any> queueInOrder(actions: Collection<RestAction<T>>): List<T> {
    return actions.map { it.await() }
}

suspend fun<T> RestAction<T>.await(): T {
    return submit().await()
}
