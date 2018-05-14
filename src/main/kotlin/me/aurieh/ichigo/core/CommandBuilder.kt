package me.aurieh.ichigo.core

import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.core.annotations.Cooldown
import me.aurieh.ichigo.core.checks.ICheck

class CommandBuilder(
        private val instance: ICommand,
        private val properties: Command,
        private val cooldown: Cooldown?
) {
    private val checks = mutableSetOf<ICheck>()

    fun addCheck(check: ICheck): CommandBuilder {
        checks.add(check)
        return this
    }

    fun <T : ICheck> addCheckAll(from: Collection<T>): CommandBuilder {
        checks.addAll(from)
        return this
    }

    fun build(): CommandWrapper {
        return CommandWrapper(instance, properties, cooldown, checks)
    }
}