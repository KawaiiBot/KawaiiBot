package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "The sacred scroll has something to say!", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Scroll : AbstractAPICommand() {
    override val path = "/image/scroll"
}