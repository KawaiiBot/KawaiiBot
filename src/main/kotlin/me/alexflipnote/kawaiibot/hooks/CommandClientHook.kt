package me.alexflipnote.kawaiibot.hooks

import me.devoxin.flight.BadArgument
import me.devoxin.flight.Context
import me.devoxin.flight.DefaultCommandClientAdapter

class CommandClientHook : DefaultCommandClientAdapter() {

    override fun onBadArgument(ctx: Context, error: BadArgument) {
        ctx.send("You need to specify a valid `${error.argument.type.simpleName.toLowerCase()}` ;-;")
    }

}