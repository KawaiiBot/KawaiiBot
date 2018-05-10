package me.aurieh.ichigo.core.cooldowns

import gnu.trove.map.hash.TLongIntHashMap
import gnu.trove.map.hash.TLongLongHashMap
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.time.temporal.ChronoUnit

class Bucket(private val rate: Int, private val per: Int, private val bucketType: BucketType, unit: ChronoUnit) {
    private val rates = TLongIntHashMap()
    private val cooldowns = TLongLongHashMap()
    private val millis = unit.duration.toMillis()

    private fun getRemainingRate(key: Long): Int {
        return rates.adjustOrPutValue(key, -1, per - 1)
    }

    private fun getRemainingCooldown(key: Long, now: Long): Long {
        if (!cooldowns.containsKey(key)) return 0
        val retryAfter = cooldowns.get(key) - now
        if (retryAfter <= 0) {
            rates.remove(key)
            cooldowns.remove(key)
            return 0
        }
        return retryAfter
    }

    fun update(event: MessageReceivedEvent): Long {
        val now = System.currentTimeMillis()
        val key = bucketType.getKey(event)
        val ticketsLeft = getRemainingRate(key)
        if (ticketsLeft > 0) return 0
        val retryAfter = getRemainingCooldown(key, now)
        if (retryAfter > 0) return retryAfter
        cooldowns.put(key, now + rate * millis)
        return 0
    }
}