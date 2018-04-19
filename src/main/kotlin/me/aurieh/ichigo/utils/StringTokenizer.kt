package me.aurieh.ichigo.utils

import gnu.trove.map.hash.TCharCharHashMap

object StringTokenizer {
    fun tokenizeToIterator(str: String, quoteChars: TCharCharHashMap = defaultQuoteChars): TokenIterator {
        return TokenIterator(str, quoteChars)
    }

    class TokenIterator internal constructor(str: String, private val quoteChars: TCharCharHashMap) : Iterator<String> {
        private val charIterator = str.iterator()
        private var str = StringBuilder()

        override fun hasNext() = charIterator.hasNext()

        override fun next(): String {
            if (!hasNext()) {
                throw NoSuchElementException()
            }

            var quoting = false
            var escaping = false
            var quoteChar = ' '

            while (charIterator.hasNext()) {
                val char = charIterator.nextChar()
                if (escaping) {
                    str.append(char)
                    escaping = false
                } else if (char == '\\') {
                    escaping = true
                } else if (quoting && char == quoteChar) {
                    quoting = false
                } else if (!quoting && quoteChars.containsKey(char)) {
                    quoting = true
                    quoteChar = quoteChars[char]
                } else if (!quoting && char.isWhitespace()) {
                    if (str.isEmpty()) continue
                    else break
                } else {
                    str.append(char)
                }
            }

            if (quoting) throw UnclosedQuoteError("unclosed quote at end of string")

            val string = str.toString()
            str = StringBuilder()
            return string
        }

        fun rest(): String {
            return charIterator.joinToString()
        }
    }

    private fun CharIterator.joinToString(): String {
        val str = StringBuilder()
        forEachRemaining { str.append(it) }
        return str.toString()
    }

    class UnclosedQuoteError(s: String) : Throwable(s)

    fun tCharCharHashMapOf(vararg pairs: Pair<Char, Char>): TCharCharHashMap {
        val map = TCharCharHashMap()
        for ((k, v) in pairs)
            map.put(k, v)
        return map
    }

    val defaultQuoteChars = tCharCharHashMapOf(
            '\'' to '\'',
            '"' to '"',
            '«' to '»',
            '‘' to '’',
            '‚' to '‛',
            '“' to '”',
            '„' to '‟',
            '‹' to '›',
            '⹂' to '⹂',
            '「' to '」',
            '『' to '』',
            '〝' to '〞',
            '﹁' to '﹂',
            '﹃' to '﹄',
            '＂' to '＂',
            '｢' to '｣',
            '«' to '»',
            '‹' to '›',
            '《' to '》',
            '〈' to '〉'
    )
}
