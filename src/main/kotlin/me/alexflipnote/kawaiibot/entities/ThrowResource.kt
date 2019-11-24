package me.alexflipnote.kawaiibot.entities

data class ThrowResource(val targetQuotes: Array<String>, val authorQuotes: Array<String>, val items: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThrowResource

        if (!targetQuotes.contentEquals(other.targetQuotes)) return false
        if (!authorQuotes.contentEquals(other.authorQuotes)) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = targetQuotes.contentHashCode()
        result = 31 * result + authorQuotes.contentHashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }
}