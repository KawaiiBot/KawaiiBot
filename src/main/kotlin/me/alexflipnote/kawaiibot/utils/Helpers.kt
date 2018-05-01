package me.alexflipnote.kawaiibot.utils

import org.json.JSONArray
import org.json.JSONObject

import java.io.InputStream
import java.util.ArrayList
import java.util.Random


object Helpers {

    private val classLoader = Helpers::class.java.classLoader
    private val random = Random()

    fun chooseRandom(array: Array<String>) = array[random.nextInt(array.size)]

    fun chooseRandom(array: JSONArray): JSONObject = array.getJSONObject(random.nextInt(array.length()))

    fun chooseRandom(list: List<String>) = list[random.nextInt(list.size)]


    fun parseTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000 % 60
        val minutes = milliseconds / (1000 * 60) % 60
        val hours = milliseconds / (1000 * 60 * 60) % 24
        val days = milliseconds / (1000 * 60 * 60 * 24)

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
    }

    fun pad(content: String, padWith: String, length: Int): String {
        if (content.length >= length)
            return content

        val sb = StringBuilder(content)

        while (sb.length < length)
            sb.append(padWith)

        return sb.toString()
    }

    fun getImageStream(filename: String): InputStream = classLoader.getResourceAsStream(filename)

    fun keyExists(json: JSONObject, key: String) = json.has(key) && !json.isNull(key)

    fun splitText(content: String, limit: Int): Array<String> {
        val pages = ArrayList<String>()

        val lines = content.trim { it <= ' ' }.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var chunk = StringBuilder()

        for (line in lines) {
            if (chunk.isNotEmpty() && chunk.length + line.length > limit) {
                pages.add(chunk.toString())
                chunk = StringBuilder()
            }

            if (line.length > limit) {
                val lineChunks = line.length / limit

                for (i in 0 until lineChunks) {
                    val start = limit * i
                    val end = start + limit
                    pages.add(line.substring(start, end))
                }
            } else {
                chunk.append(line).append("\n")
            }
        }

        if (chunk.isNotEmpty())
            pages.add(chunk.toString())

        return pages.toTypedArray()

    }

}
