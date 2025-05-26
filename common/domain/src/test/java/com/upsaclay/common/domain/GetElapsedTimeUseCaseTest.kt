package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class GetElapsedTimeUseCaseTest {
    private val getElapsedTimeUseCase = GetElapsedTimeUseCase

    @Test
    fun fromLocalDateTime_should_return_elapsed_time_now_when_duration_is_inferior_to_1_minute() {
        // Given
        val localDateTime = LocalDateTime.now()

        // When
        val result = getElapsedTimeUseCase.fromLocalDateTime(localDateTime)

        // Then
        assert(result is ElapsedTime.Now)
    }

    @Test
    fun fromLocalDateTime_should_return_elapsed_time_minute_when_duration_is_inferior_to_1_hour() {
        // Given
        val localDateTime = LocalDateTime.now().minusMinutes(30)

        // When
        val result = getElapsedTimeUseCase.fromLocalDateTime(localDateTime)

        // Then
        assertEquals(ElapsedTime.Minute(30), result)
    }

    @Test
    fun fromLocalDateTime_should_return_elapsed_time_hour_when_duration_is_between_1_and_24_hours() {
        // Given
        val localDateTime = LocalDateTime.now().minusHours(5)

        // When
        val result = getElapsedTimeUseCase.fromLocalDateTime(localDateTime)

        // Then
        assertEquals(ElapsedTime.Hour(5), result)
    }

    @Test
    fun fromLocalDateTime_should_return_elapsed_time_day_when_duration_is_between_1_and_7_days() {
        // Given
        val localDateTime = LocalDateTime.now().minusDays(2)

        // When
        val result = getElapsedTimeUseCase.fromLocalDateTime(localDateTime)

        // Then
        assertEquals(ElapsedTime.Day(2), result)
    }

    @Test
    fun fromLocalDateTime_should_return_elapsed_time_week_when_duration_is_between_7_to_30_days() {
        // Given
        val localDateTime = LocalDateTime.now().minusDays(14)

        // When
        val result = getElapsedTimeUseCase.fromLocalDateTime(localDateTime)

        // Then
        assertEquals(ElapsedTime.Week(2), result)
    }

    @Test
    fun fromLocalDateTime_should_return_date_when_duration_is_superior_to_30_days() {
        // Given
        val localDateTime = LocalDateTime.now().minusDays(60)

        // When
        val result = getElapsedTimeUseCase.fromLocalDateTime(localDateTime)

        // Then
        assertEquals(ElapsedTime.Later(localDateTime), result)
    }
}