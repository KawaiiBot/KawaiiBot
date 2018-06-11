package me.alexflipnote.kawaiibot.commands

import kotlinx.coroutines.experimental.Unconfined
import me.alexflipnote.kawaiibot.KawaiiBot
import me.alexflipnote.kawaiibot.entities.Responses
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.CoroutineCommand
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.extensions.await
import net.dv8tion.jda.core.Permission

@Command(description = "Unbans a user from the current server.", guildOnly = true, userPermissions = [Permission.BAN_MEMBERS], botPermissions = [Permission.BAN_MEMBERS])
class Unban : CoroutineCommand(Unconfined) {
    override suspend fun execute(ctx: CommandContext) {
        val member = ctx.args.nextSnowflakeOrNull() ?: return ctx.send("Give me someone to unban ;-;")
        val bans = ctx.guild!!.banList.await()
        val isUserBanned = bans.any { it.user.idLong == member }

        if (!isUserBanned) {
            return ctx.send("It looks like that user isn't banned...")
        }

        val reason = ctx.args.collect()
        ctx.guild.controller
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
