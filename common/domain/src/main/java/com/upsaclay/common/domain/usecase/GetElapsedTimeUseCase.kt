package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.ElapsedTime
import java.time.Duration
import java.time.LocalDateTime

object GetElapsedTimeUseCase {
    fun fromLocalDateTime(localDateTime: LocalDateTime): ElapsedTime {
        val duration = Duration.between(localDateTime, LocalDateTime.now())
        return when {
            duration.toMinutes() < 1 -> ElapsedTime.Now(duration.seconds)
            duration.toMinutes() < 60 -> ElapsedTime.Minute(duration.toMinutes())
            duration.toHours() < 24 -> ElapsedTime.Hour(duration.toHours())
            duration.toDays() < 7 -> ElapsedTime.Day(duration.toDays())
            duration.toDays() < 30 -> ElapsedTime.Week(duration.toDays() / 7)
            else -> ElapsedTime.Later(localDateTime)
        }
    }
}