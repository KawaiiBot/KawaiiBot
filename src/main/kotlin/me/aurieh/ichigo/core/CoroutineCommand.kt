package me.aurieh.ichigo.core

import kotlinx.coroutines.experimental.async
import me.aurieh.ichigo.utils.OptParser
import me.aurieh.ichigo.utils.StringTokenizer
import kotlin.coroutines.experimental.CoroutineContext

abstract class CoroutineCommand(private val context: CoroutineContext? = null) : ICommand {
    abstract suspend fun execute(ctx: CommandContext)

    final override fun run(ctx: CommandContext) {
        async(context ?: ctx.commandHandler.coroutineContext) {
            try {
                execute(ctx)
            } catch (e: Arguments.InvalidArgumentError) { // TODO: undupe @ CommandHandler
                ctx.send("**You have an invalid/missing argument:**\n```${e.message}```")
            } catch (e: OptParser.EmptyKeyError) {
                ctx.send("**You have an empty flag key**")
            } catch (e: StringTokenizer.UnclosedQuoteError) {
                ctx.send(CommandHandler.UNCLOSED_QUOTE_ERROR)
            } catch (e: Throwable) {
                CommandHandler.LOG.error("asynchronous command runner raised an exception", e)
            }
        }
    }
}
