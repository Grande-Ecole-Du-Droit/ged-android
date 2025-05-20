package com.upsaclay.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormatLocalDateTimeUseCase {
    fun formatDayMonthYear(localDateTime: LocalDateTime): String {
        val formatter = if (Locale.getDefault().language == "fr") {
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH)
        } else {
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        }
        return localDateTime.format(formatter)
    }

    fun formatHourMinute(localDateTime: LocalDateTime): String {
        val formatter = if (Locale.getDefault().language == "fr") {
            DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH)
        } else {
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        }
        return localDateTime.format(formatter)
    }
}