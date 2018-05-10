package me.aurieh.ichigo.core.waiter

import me.aurieh.ichigo.extensions.futureOf
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.timerTask

/**
 * This implementation is NOT thread-safe
 */
class EventWaiter : ListenerAdapter() {
    private val listeners = mutableMapOf<Class<out Event>, MutableSet<Listener<*>>>()
    private val timer = Timer()

    val listenersCount
        get() = listeners.map { it.value.size }.sum()

    override fun onGenericEvent(event: Event) {
        val clazz = event::class.java
        listeners[clazz]?.removeIf {
            @Suppress("UNCHECKED_CAST")
            (it as Listener<Event>).pass(event)
            it.remove(event)
        }
    }

    fun <T : Event> addListener(clazz: Class<T>, listener: Listener<T>): EventWaiter {
        synchronized(listeners) {
            listeners.computeIfAbsent(clazz, { mutableSetOf() }).add(listener)
        }
        return this
    }

    inline fun <reified T : Event> addListener(listener: Listener<T>): EventWaiter {
        return addListener(T::class.java, listener)
    }

    fun <T : Event> waitFor(clazz: Class<T>, timeout: Long = -1, check: ((T) -> Boolean)? = null) = futureOf<T> {
        val listener = object : Listener<T> {
            override fun pass(e: T) {
                try {
                    if (!isDone && check?.invoke(e) == true) {
                        complete(e)
                    }
                } catch (e: Throwable) {
                    completeExceptionally(e)
                }
            }

            override fun remove(e: T): Boolean {
                return isDone
            }
        }
        addListener(clazz, listener)
        if (timeout > 0) timer.schedule(timerTask { cancel(true) }, timeout)
    }

    inline fun <reified T : Event> waitFor(timeout: Long = -1, noinline check: ((T) -> Boolean)? = null): CompletableFuture<T> {
        return waitFor(T::class.java, timeout, check)
    }

    interface Listener<in T : Event> {
        fun pass(e: T)
        fun remove(e: T): Boolean
    }
}