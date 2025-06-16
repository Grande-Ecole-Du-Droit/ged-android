package com.upsaclay.common.domain.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

private fun LocalDateTime.toInstantUTC(): Instant = this.atZone(ZoneOffset.UTC).toInstant()

fun LocalDateTime.toEpochMilliUTC(): Long = this.toInstantUTC().toEpochMilli()

fun Instant.toLocalDateTimeUTC(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneOffset.UTC)

fun Long.toLocalDateTimeUTC(): LocalDateTime = Instant.ofEpochMilli(this).toLocalDateTimeUTC()