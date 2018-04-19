package me.alexflipnote.kawaiibot.entities

import java.util.*

data class ThrowResource(val targetQuotes: Array<String>, val authorQuotes: Array<String>, val items: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThrowResource

        if (!Arrays.equals(targetQuotes, other.targetQuotes)) return false
        if (!Arrays.equals(authorQuotes, other.authorQuotes)) return false
        if (!Arrays.equals(items, other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(targetQuotes)
        result = 31 * result + Arrays.hashCode(authorQuotes)
        result = 31 * result + Arrays.hashCode(items)
        return result
    }
}