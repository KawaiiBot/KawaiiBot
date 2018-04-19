package me.aurieh.ichigo.utils

import me.aurieh.ichigo.extensions.splitOnFirst

object OptParser {
    /**
     * Tokenize a string. Quoted strings are preserved. Escaped quotes aren't interpreted.
     * @throws UnclosedQuoteError
     *
     * @return tokens
     */
    fun tokenize(str: String): List<String> {
        val tokens = mutableListOf<String>()
        var escaping = false
        var quoteChar = ' '
        var quoting = false
        var intermediate = StringBuilder()
        val iter = str.iterator()
        while (iter.hasNext()) {
            val char = iter.next()
            if (escaping) {
                intermediate.append(char)
                escaping = false
            } else if (char == '\\' && !(quoting && quoteChar == '\'')) {
                escaping = true
            } else if (quoting && char == quoteChar) {
                quoting = false
            } else if (!quoting && (char == '\'' || char == '"')) {
                quoting = true
                quoteChar = char
            } else if (!quoting && char.isWhitespace()) {
                if (intermediate.isNotEmpty()) {
                    tokens.add(intermediate.toString())
                    intermediate = StringBuilder()
                }
            } else {
                intermediate.append(char)
            }
        }
        if (quoting) {
            throw UnclosedQuoteError("unclosed quote")
        }
        if (intermediate.isNotEmpty()) {
            tokens.add(intermediate.toString())
        }
        return tokens
    }

    /**
     * Parses POSIX-like flags (untyped version)
     * @throws EmptyKeyError
     *
     * @return parsed flags (and unmatched data)
     */
    fun parsePosix(tokens: List<String>): ParsedResult {
        val unmatched = mutableListOf<String>()
        val argMap = mutableMapOf<String, String?>()
        var nextKey: String? = null

        val tokenIter = tokens.iterator()

        for (token in tokenIter) {
            if (token == "--") {
                break
            } else if (token.startsWith('-') && token != "-") {
                if (nextKey != null) {
                    argMap[nextKey] = null
                    nextKey = null
                }

                val cut = if (token.startsWith("--")) 2 else 1
                val kv = token.drop(cut)

                val (k, v) = kv.splitOnFirst('=')
                if (k.isEmpty()) {
                    throw EmptyKeyError("empty key while reading \"$token\"", token)
                }

                if (cut > 1) { // long form
                    if (v.isEmpty()) {
                        nextKey = k
                    } else {
                        argMap[k] = v
                    }
                } else { // short form
                    // FYI, this doesn't match -ovalue where o is flag shorthand
                    val lastK = k.last().toString()
                    if (v.isEmpty()) {
                        nextKey = lastK
                    } else {
                        argMap[lastK] = v
                    }
                    val head = k.dropLast(1).map { it.toString() }
                    head.forEach { argMap[it] = null }
                }
            } else if (nextKey != null) {
                argMap[nextKey] = token
                nextKey = null
            } else {
                unmatched.add(token)
            }
        }

        unmatched.addAll(tokenIter.asSequence())

        return ParsedResult(unmatched, argMap)
    }

    class UnclosedQuoteError(msg: String) : Exception(msg)

    class EmptyKeyError(msg: String, val token: String) : Exception(msg)

    data class ParsedResult(val unmatched: List<String>, val argMap: Map<String, String?>)
}