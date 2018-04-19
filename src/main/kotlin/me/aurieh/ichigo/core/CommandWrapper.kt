package me.aurieh.ichigo.core

import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.core.annotations.Cooldown
import me.aurieh.ichigo.core.checks.ICheck
import me.aurieh.ichigo.core.cooldowns.Bucket
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class CommandWrapper internal constructor(command: ICommand, val properties: Command, cooldown: Cooldown? = null, val checks: Collection<ICheck>) : ICommand by command {
    val name = command::class.java.simpleName.toLowerCase()
    internal val bucket: Bucket? = if (cooldown != null && cooldown.per > 0)
        Bucket(cooldown.rate, cooldown.per, cooldown.bucket, cooldown.unit)
    else null

    internal fun canSelfInteract(e: MessageReceivedEvent): List<Permission> {
        if (e.guild == null) return emptyList()
        return properties.botPermissions.filter {
            !e.guild.selfMember.hasPermission(e.textChannel, it)
        }
    }

    internal fun canUserInteract(e: MessageReceivedEvent): List<Permission> {
        if (e.member == null) return emptyList()
        return properties.userPermissions.filter {
            !e.member.hasPermission(e.textChannel, it)
        }
    }
}
