package me.aurieh.ichigo.core.annotations

import net.dv8tion.jda.core.Permission

@Target(AnnotationTarget.CLASS)
annotation class Command(
        val aliases: Array<String> = [],
        val description: String = "No description available",
        val usage: String = "No usage available",
        val botPermissions: Array<Permission> = [],
        val userPermissions: Array<Permission> = [],
        val guildOnly: Boolean = false,
        val developerOnly: Boolean = false,
        val hidden: Boolean = false,
        val isNSFW: Boolean = false
)
