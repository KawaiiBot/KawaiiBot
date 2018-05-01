package me.alexflipnote.kawaiibot.utils

import org.json.JSONArray
import org.json.JSONObject

object NSFWCheck {
    private val ILLEGAL_NSFW = ResourceUtil.readJson<HashSet<String>>("constants/illegalNSFW.json")

    fun check(items: List<String>): Boolean {
        for (item in items)
            if (ILLEGAL_NSFW.contains(item.toLowerCase()))
                return false
        return true
    }

    fun filterJSON(json: JSONArray): JSONArray {
        json.asIterable().forEachIndexed { i, it ->
            if (it is JSONObject)
                for (tag in it.getString("tags").split(" "))
                    if (ILLEGAL_NSFW.contains(tag.toLowerCase()))
                        json.remove(i)
        }
        return json
    }
}