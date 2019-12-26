package me.alexflipnote.kawaiibot.hooks

import me.alexflipnote.kawaiibot.KawaiiBot
import me.devoxin.flight.api.CommandWrapper
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.DefaultCommandClientAdapter
import me.devoxin.flight.exceptions.BadArgument
import net.dv8tion.jda.api.Permission

class CommandClientHook : DefaultCommandClientAdapter() {

    override fun onBadArgument(ctx: Context, command: CommandWrapper, error: BadArgument) {
        ctx.send("You need to specify something for `${error.argument.name}` ;-;")
    }

    override fun onBotMissingPermissions(ctx: Context, command: CommandWrapper, permissions: List<Permission>) {
        ctx.send("I don't have enough permissions... ;-;\n\n" +
                "`Missing:`\n${permissions.joinToString("\n")}")
    }

    override fun onUserMissingPermissions(ctx: Context, command: CommandWrapper, permissions: List<Permission>) {
        ctx.send("You don't have enough permissions... ;-;\n\n" +
                "`Missing:`\n${permissions.joinToString("\n")}")
    }

    override fun onCommandError(ctx: Context, command: CommandWrapper, error: Throwable) {
        KawaiiBot.logger.error("An error occurred during execution of command `${command.name}`", error)
        ctx.send("An error occurred during the command... I'm sorry ;-; I've informed my developers~")
    }

    override fun onParseError(ctx: Context, command: CommandWrapper, error: Throwable) {
        KawaiiBot.logger.error("An error occurred during argument parsing", error)
        ctx.send("Something happened during command execution... sorry ;-;' I've informed my developers~")
    }

}