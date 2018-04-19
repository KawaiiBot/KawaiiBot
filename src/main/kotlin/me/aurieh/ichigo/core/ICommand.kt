package me.aurieh.ichigo.core

interface ICommand {
    fun run(ctx: CommandContext)
}