package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Get those achievements like in Minecraft!", aliases = ["ah"], botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Achievement : AbstractAPICommand() {
    override val path = "/achievement"
}