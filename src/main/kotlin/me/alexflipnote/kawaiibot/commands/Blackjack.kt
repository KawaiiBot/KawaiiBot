package me.alexflipnote.kawaiibot.commands

import me.alexflipnote.kawaiibot.utils.BlackjackSession
import me.aurieh.ichigo.core.CommandContext
import me.aurieh.ichigo.core.CoroutineCommand
import me.aurieh.ichigo.core.annotations.Command

@Command(description = "Play Blackjack!")
class Blackjack : CoroutineCommand() {
    override suspend fun execute(ctx: CommandContext) {
        BlackjackSession(ctx).start()

    }
}