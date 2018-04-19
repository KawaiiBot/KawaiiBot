package me.aurieh.ichigo.core

import com.google.common.collect.ImmutableList
import me.aurieh.ichigo.extensions.resolveNames
import me.aurieh.ichigo.utils.OptParser
import me.aurieh.ichigo.utils.StringTokenizer
import me.aurieh.ichigo.utils.StringUtil
import me.aurieh.ichigo.utils.StringUtil.userMentionPattern
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

/**
 * An argument container.
 *
 * @property bySpace arguments split by space
 * @property asString arguments joined to a string
 * @property asDisplayString arguments joined to a string and resolved with names
 * @property asCleanString same as calling StringUtil.cleanContent with asString
 * @property asCleanerString same as calling StringUtil.cleanerContent with asString
 * @property asFlags returns posix-like flags and unmatched content
 * @property asMember convenience prop that parses the entire arg string as a member
 */
class Arguments internal constructor(private val asIter: StringTokenizer.TokenIterator, private val receivedEvent: MessageReceivedEvent) : Iterator<String> by asIter {
    val bySpace by lazy { ImmutableList.copyOf(this) }
    val asString by lazy { asIter.rest() }
    val asDisplayString by lazy { receivedEvent.jda.resolveNames(bySpace) }
    val asCleanString by lazy { StringUtil.cleanContent(asString) }
    val asCleanerString by lazy { StringUtil.cleanerContent(asString) }
    val asFlags by lazy { OptParser.parsePosix(bySpace) }
    val asMember by lazy { convertMember(asString) }

    /**
     * Same as checking if hasNext() and then calling next()
     * @return string or null
     * @see hasNext
     * @see next
     */
    fun nextOrNull(): String? {
        return if (hasNext()) asIter.next() else null
    }

    /**
     * Same as calling asSequence().joinToString()
     * @return string
     */
    fun collect(): String {
        return asIter.rest()
    }

    /**
     * @return self
     */
    fun iterator(): Arguments {
        return this
    }

    /**
     * Next argument parsed as an int
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     */
    fun nextInt(): Int {
        try {
            return next().toInt()
        } catch (e: NumberFormatException) {
            throw InvalidArgumentError("badly formatted integer value")
        } catch (e: NoSuchElementException) {
            throw InvalidArgumentError("missing integer value", e)
        }
    }

    /**
     * Collect rest of elements into a string and parse that
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     */
    fun collectInt(): Int {
        try {
            return collect().replace(" ", "").toInt()
        } catch (e: NumberFormatException) {
            // TODO: range
            throw InvalidArgumentError("badly formatted integer value")
        }
    }

    /**
     * Next argument as int or null if unavailable or invalid
     */
    fun nextIntOrNull(): Int? {
        return nextOrNull()?.toIntOrNull()
    }

    /**
     * Next argument parsed as a float
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     */
    fun nextFloat(): Float {
        try {
            return next().toFloat()
        } catch (e: NumberFormatException) {
            throw InvalidArgumentError("badly formatted float value", e)
        }
    }

    /**
     * Collect rest of elements into a string and parse that
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     */
    fun collectFloat(): Float {
        try {
            return collect().replace(" ", "").toFloat()
        } catch (e: NumberFormatException) {
            throw InvalidArgumentError("badly formatted float value", e)
        }
    }

    /**
     * Next argument as float or null if unavailable or invalid
     */
    fun nextFloatOrNull(): Float? {
        return nextOrNull()?.toFloatOrNull()
    }

    /**
     * Next argument parsed as a boolean
     *
     * It also parses yes/no, on/off, enabled/disabled
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     *
     * @see BOOLEAN_TRUE
     * @see BOOLEAN_FALSE
     */
    fun nextBoolean(): Boolean {
        try {
            return convertBoolean(next())
        } catch (e: NoSuchElementException) {
            throw InvalidArgumentError("missing boolean value", e)
        }
    }

    /**
     * Collect rest of elements into a string and parse that
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     *
     * @see nextBoolean
     */
    fun collectBoolean(): Boolean {
        return convertBoolean(collect())
    }

    /**
     * Next argument as boolean or null if unavailable or invalid
     *
     * @see nextBoolean
     */
    fun nextBooleanOrNull(): Boolean? {
        return try {
            convertBoolean(nextOrNull() ?: return null)
        } catch (e: InvalidArgumentError) {
            null
        }
    }

    /**
     * Next argument resolved as a member
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     *
     * @see userMentionPattern
     * @see userDiscrimPattern
     */
    fun nextMember(): Member {
        try {
            return convertMember(next())
                    ?: throw InvalidArgumentError("badly formatted member (use mention, id or name)")
        } catch (e: NoSuchElementException) {
            throw InvalidArgumentError("missing member", e)
        }
    }

    /**
     * Collect rest of elements into a string and resolve that
     * @throws InvalidArgumentError
     * @throws NoSuchElementException
     *
     * @see userMentionPattern
     * @see userDiscrimPattern
     */
    fun collectMember(): Member {
        return convertMember(collect()) ?: throw InvalidArgumentError("badly formatted member")
    }

    /**
     * Next argument as member or null if unavailable or invalid
     *
     * @see userMentionPattern
     * @see userDiscrimPattern
     */
    fun nextMemberOrNull(): Member? {
        return convertMember(nextOrNull() ?: return null)
                ?: throw InvalidArgumentError("badly formatted member (use mention, id or name)")
    }

    private fun convertBoolean(str: String): Boolean {
        if (str.startsWith("t") || str.startsWith("y") || BOOLEAN_TRUE.contains(str))
            return true
        if (str.startsWith("f") || str.startsWith("n") || BOOLEAN_FALSE.contains(str))
            return false
        throw InvalidArgumentError("badly formatted boolean value")
    }

    private fun convertMember(str: String): Member? {
        if (str.isEmpty()) return null
        val id = userMentionPattern.find(str)?.groups?.get(1)?.value ?: str
        val member = try {
            receivedEvent.guild!!.getMemberById(id)
        } catch (e: NumberFormatException) {
            null
        } catch (e: NullPointerException) {
            throw InvalidArgumentError("this command may only be used in a guild")
        }
        if (member != null) return member
        val (username, discrim) = convertUsernameDiscrim(str)
        return if (discrim != null)
            receivedEvent.guild.members.find { it.user.name == username && it.user.discriminator == discrim }
        else
            receivedEvent.guild.members.find { it.user.name == str }
    }

    private fun convertUsernameDiscrim(str: String): Pair<String?, String?> {
        return userDiscrimPattern.find(str).let {
            if (it == null)
                nullToNull
            else
                it.groups[1]?.value to it.groups[2]?.value
        }
    }

    class InvalidArgumentError(msg: String, e: Throwable? = null, val index: Int = -1) : Exception(msg, e)

    companion object {
        val BOOLEAN_TRUE = arrayOf("on", "enable", "enabled")
        val BOOLEAN_FALSE = arrayOf("off", "disable", "disabled")
        val userDiscrimPattern = Regex("(.{1,32})#(\\d{4})")
        val nullToNull = null to null
    }
}