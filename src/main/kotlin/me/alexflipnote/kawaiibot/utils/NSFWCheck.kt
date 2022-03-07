package me.alexflipnote.kawaiibot.utils

import org.json.JSONArray
import org.json.JSONObject

object NSFWCheck {
    private val ILLEGAL_NSFW = ResourceUtil.readJson<HashSet<String>>("constants/illegalNSFW.json")

    fun check(items: Iterable<String>): Boolean = ILLEGAL_NSFW.intersect(items.map { it.toLowerCase() }.toSet()).isEmpty()

    fun filterJSON(json: JSONArray): JSONArray {
        json.asIterable().forEachIndexed { i, it ->
            it as JSONObject
            if (!check(it.getString("tags").split(" ")))
                json.remove(i)
        }
        return json
    }
}