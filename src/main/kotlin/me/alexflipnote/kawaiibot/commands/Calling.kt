package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Tom & Jerry calling meme generator", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Calling : AbstractAPICommand() {
    override val path = "/calling"
}