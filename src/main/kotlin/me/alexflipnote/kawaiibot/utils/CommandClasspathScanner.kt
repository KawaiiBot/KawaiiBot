package me.alexflipnote.kawaiibot.utils

import com.google.common.collect.MutableClassToInstanceMap
import com.google.common.reflect.ClassPath
import me.alexflipnote.kawaiibot.KawaiiBot
import me.aurieh.ichigo.core.CommandBuilder
import me.aurieh.ichigo.core.CommandWrapper
import me.aurieh.ichigo.core.ICommand
import me.aurieh.ichigo.core.annotations.Check
import me.aurieh.ichigo.core.annotations.Command
import me.aurieh.ichigo.core.annotations.Cooldown
import me.aurieh.ichigo.core.checks.ICheck

object CommandClasspathScanner {
    private val checksMap = MutableClassToInstanceMap.create<ICheck>()!!

    fun scan(classLoader: ClassLoader): List<CommandWrapper> {
        val commands = mutableListOf<CommandWrapper>()
        val classpath = ClassPath.from(classLoader)
        val topLevelClasses = classpath.getTopLevelClasses("me.alexflipnote.kawaiibot.commands")
        for (klass in topLevelClasses) {
            val clazz = klass.load()
            if (clazz.isAnnotation || clazz.isInterface || !ICommand::class.java.isAssignableFrom(clazz))
                continue
            val annotation = clazz.annotations.find { it is Command } as? Command
            if (annotation == null) {
                KawaiiBot.LOG.warn("${clazz.canonicalName} implements ICommand, but isn't annotated with Command")
                continue
            }
            val cooldown = clazz.annotations.find { it is Cooldown } as? Cooldown
            val builder = try {
                val instance = clazz.newInstance() as ICommand
                CommandBuilder(instance, annotation, cooldown)
            } catch (e: Throwable) {
                KawaiiBot.LOG.error("error while creating instance of ${clazz.simpleName}", e)
                continue
            }
            val checks = try {
                clazz.annotations
                        .filter { it is Check }
                        .map { (it as Check).checker.java }
                        .map { checksMap.computeIfAbsent(it) { it.newInstance() } }
            } catch (e: Throwable) {
                KawaiiBot.LOG.error("error while creating instance of check for ${clazz.simpleName}", e)
                continue
            }
            commands.add(builder.addCheckAll(checks).build())
        }
        return commands
    }
}