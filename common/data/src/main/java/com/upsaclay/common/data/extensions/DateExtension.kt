package com.upsaclay.common.data.extensions

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toTimestamp(): Timestamp =
    Timestamp(this.atZone(ZoneOffset.UTC).toInstant())

fun Timestamp.toLocalDateTime(): LocalDateTime = this.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime()