package me.alexflipnote.kawaiibot.utils

object NSFWCheck {
    private val ILLEGAL_NSFW = ResourceUtil.readJson<HashSet<String>>("constants/illegalNSFW.json")

    fun check(items: List<String>): Boolean {
        for (item in items)
            if (ILLEGAL_NSFW.contains(item.toLowerCase()))
                return false
        return true
    }
}