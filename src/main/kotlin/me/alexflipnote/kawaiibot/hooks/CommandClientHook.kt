package me.alexflipnote.kawaiibot.hooks

import me.alexflipnote.kawaiibot.KawaiiBot
import me.devoxin.flight.api.CommandFunction
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.exceptions.BadArgument
import me.devoxin.flight.api.hooks.CommandEventAdapter
import net.dv8tion.jda.api.Permission

class CommandClientHook : CommandEventAdapter {
    override fun onBadArgument(ctx: Context, command: CommandFunction, error: BadArgument) {
        ctx.send("You need to specify something for `${error.argument.name}` ;-;")
    }

    override fun onBotMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) {
        ctx.send("I don't have enough permissions... ;-;\n\n" +
                "`Missing:`\n${permissions.joinToString("\n")}")
    }

    override fun onUserMissingPermissions(ctx: Context, command: CommandFunction, permissions: List<Permission>) {
        ctx.send("You don't have enough permissions... ;-;\n\n" +
                "`Missing:`\n${permissions.joinToString("\n")}")
    }

    override fun onCommandError(ctx: Context, command: CommandFunction, error: Throwable) {
        KawaiiBot.logger.error("An error occurred during execution of command `${command.name}`", error)
        ctx.send("An error occurred during the command... I'm sorry ;-; I've informed my developers~")
    }

    override fun onParseError(ctx: Context, command: CommandFunction, error: Throwable) {
        KawaiiBot.logger.error("An error occurred during argument parsing", error)
        ctx.send("Something happened during command execution... sorry ;-;' I've informed my developers~")
    }

    override fun onCommandCooldown(ctx: Context, command: CommandFunction, cooldown: Long) {

    }

    override fun onCommandPostInvoke(ctx: Context, command: CommandFunction, failed: Boolean) {

    }

    override fun onCommandPreInvoke(ctx: Context, command: CommandFunction) = true
}
