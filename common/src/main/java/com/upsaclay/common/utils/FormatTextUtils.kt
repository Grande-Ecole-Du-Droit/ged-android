package com.upsaclay.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import java.time.LocalDateTime

@Composable
fun getElapsedTimeValue(date: LocalDateTime): String {
    return when (val elapsedTime = GetElapsedTimeUseCase.fromLocalDateTime(date)) {
        is ElapsedTime.Now -> stringResource(com.upsaclay.common.R.string.now, elapsedTime.value)
        is ElapsedTime.Minute -> stringResource(com.upsaclay.common.R.string.minute_ago_short, elapsedTime.value)
        is ElapsedTime.Hour -> stringResource(com.upsaclay.common.R.string.hour_ago_short, elapsedTime.value)
        is ElapsedTime.Day -> stringResource(com.upsaclay.common.R.string.day_ago_short, elapsedTime.value)
        is ElapsedTime.Week -> stringResource(com.upsaclay.common.R.string.week_ago_short, elapsedTime.value)
        is ElapsedTime.Later -> FormatLocalDateTimeUseCase.formatDayMonthYear(elapsedTime.value)
    }
}