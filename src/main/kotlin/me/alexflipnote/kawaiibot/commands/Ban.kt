package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.entities.Responses
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Bans a user from the current server.", guildOnly = true, userPermissions = [Permission.BAN_MEMBERS], botPermissions = [Permission.BAN_MEMBERS])
class Ban : ICommand {
    override fun run(ctx: CommandContext) {
        val user = ctx.args.nextSnowflakeOrNull() ?: return ctx.send("Give me someone to ban ;-;")
        val member = ctx.guild!!.getMemberById(user)
        if (member != null && !ctx.member!!.canInteract(member)) {
            return ctx.send("I can't interact with this user ;-;")
        }
        val reason = ctx.args.collect()
        ctx.guild.controller
                .ban(user.toString(), 0)
                .reason(Responses.responsible(ctx.author, reason))
                .queue({
                    ctx.send(Responses.action("banned"))
                }) {
                    KawaiiBot.LOG.error("ban error", it)
                    ctx.send("There was a problem performing this action.")
                }
    }
}
