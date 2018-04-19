package me.aurieh.ichigo.core.annotations

import me.aurieh.ichigo.core.cooldowns.BucketType
import java.time.temporal.ChronoUnit

@Target(AnnotationTarget.CLASS)
annotation class Cooldown(
        val rate: Int = 1,
        val per: Int = 0,
        val unit: ChronoUnit = ChronoUnit.SECONDS,
        val bucket: BucketType = BucketType.USER
)
