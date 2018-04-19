package me.aurieh.ichigo.core.annotations

import me.aurieh.ichigo.core.checks.ICheck
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Check(val checker: KClass<out ICheck>)