package com.upsaclay.common.domain.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toInstant(): Instant = this.atZone(ZoneOffset.UTC).toInstant()

fun LocalDateTime.toLong(): Long = this.toInstant(ZoneOffset.UTC).toEpochMilli()

fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneOffset.UTC)

fun Long.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDateTime()

