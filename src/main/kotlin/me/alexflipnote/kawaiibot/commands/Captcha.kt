package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.entities.AbstractAPICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Prove someone with a captcha", botPermissions = [Permission.MESSAGE_ATTACH_FILES])
class Captcha : AbstractAPICommand() {
    override val path = "/image/captcha"
}