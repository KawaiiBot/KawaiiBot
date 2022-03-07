package me.alexflipnote.kawaiibot.utils

import com.google.gson.Gson
import java.io.IOException
import java.io.InputStreamReader

object ResourceUtil {
    val gson = Gson()
    private val classLoader = this.javaClass.classLoader!!

    @Throws(IOException::class)
    fun getResource(path: String) = classLoader.getResourceAsStream(path)!!

    inline fun <reified T : Any> readJson(path: String): T {
        return gson.fromJson(InputStreamReader(getResource(path)), T::class.java)
    }
}