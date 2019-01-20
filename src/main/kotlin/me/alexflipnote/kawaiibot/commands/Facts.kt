package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Boost your ego by making everything you say a fact", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Facts : AbstractAPICommand() {
    override val path = "/facts"
}