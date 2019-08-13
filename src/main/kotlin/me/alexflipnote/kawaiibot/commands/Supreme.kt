package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Flex on those haters with ~~fake~~ Supreme merch", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Supreme : AbstractAPICommand() {
    override val path = "/supreme"
}
