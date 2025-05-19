package com.upsaclay.common

import com.upsaclay.common.utils.FormatLocalDateTimeUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.util.Locale

class FormatLocalDateTimeUseCaseTest {
    private val formatLocalDateTimeUseCase = FormatLocalDateTimeUseCase
    private val localDateTime = LocalDateTime.of(2021, 9, 1, 12, 0, 0)

    @Test
    fun formatDayMonthYear_should_return_day_month_year_format() {
        // When
        val result = formatLocalDateTimeUseCase.formatDayMonthYear(localDateTime)

        // Then
        if (Locale.getDefault().language == "fr") {
            assertEquals("01 sept. 2021", result)
        } else {
            assertEquals("Sep 01, 2021", result)
        }
    }

    @Test
    fun formatHourMinute_should_return_hour_minute_format() {
        // When
        val result = formatLocalDateTimeUseCase.formatHourMinute(localDateTime)

        // Then
        if (Locale.getDefault().language == "fr") {
            assertEquals("12:00", result)
        } else {
            assertEquals("12:00 PM", result)
        }
    }
}