package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.entities.Responses
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import net.dv8tion.jda.core.Permission

@Command(description = "Unbans a user from the current server.", guildOnly = true, userPermissions = [Permission.BAN_MEMBERS], botPermissions = [Permission.BAN_MEMBERS])
class Unban : ICommand {
    override fun run(ctx: CommandContext) {
        val member = ctx.args.nextSnowflakeOrNull() ?: return ctx.send("Give me someone to unban ;-;")
        val reason = ctx.args.collect()
        ctx.guild!!.controller
                .unban(member.toString())
                .reason(Responses.responsible(ctx.author, reason))
                .queue({
                    ctx.send(Responses.action("unbanned"))
                }) {
                    KawaiiBot.LOG.error("unban error", it)
                    ctx.send("There was a problem performing this action.")
                }
    }
}
