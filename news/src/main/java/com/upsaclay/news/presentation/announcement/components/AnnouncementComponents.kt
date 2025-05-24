package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.FormatLocalDateTimeUseCase
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState

@Composable
internal fun AnnouncementHeader(
    modifier: Modifier = Modifier,
    announcement: Announcement
) {
    val elapsedTime = GetElapsedTimeUseCase.fromLocalDateTime(announcement.date)

    val elapsedTimeValue: String = when (elapsedTime) {
        is ElapsedTime.Now -> stringResource(
            com.upsaclay.common.R.string.now,
            elapsedTime.value
        )

        is ElapsedTime.Minute -> stringResource(
            com.upsaclay.common.R.string.minute_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Hour -> stringResource(
            com.upsaclay.common.R.string.hour_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Day -> stringResource(
            com.upsaclay.common.R.string.day_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Week -> stringResource(
            com.upsaclay.common.R.string.week_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Later -> FormatLocalDateTimeUseCase.formatDayMonthYear(elapsedTime.value)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            url = announcement.author.profilePictureFileName,
            scale = 0.45f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            modifier = Modifier.weight(fill = false, weight = 1f),
            text = announcement.author.fullName,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = elapsedTimeValue,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.previewText
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))
    }
}

@Composable
internal fun AnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit
) {
    val elapsedTime = GetElapsedTimeUseCase.fromLocalDateTime(announcement.date)

    val elapsedTimeValue = when (elapsedTime) {
        is ElapsedTime.Now -> stringResource(
            com.upsaclay.common.R.string.now,
            elapsedTime.value
        )

        is ElapsedTime.Minute -> stringResource(
            com.upsaclay.common.R.string.minute_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Hour -> stringResource(
            com.upsaclay.common.R.string.hour_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Day -> stringResource(
            com.upsaclay.common.R.string.day_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Week -> stringResource(
            com.upsaclay.common.R.string.week_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Later -> FormatLocalDateTimeUseCase.formatDayMonthYear(elapsedTime.value)
    }

    val alpha = if (announcement.state == AnnouncementState.SENDING) 0.5f else 1f

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.smallMedium)
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) {
            ProfilePicture(
                url = announcement.author.profilePictureFileName,
                scale = 0.5f
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = announcement.author.fullName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(fill = false, weight = 1f)
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                    Text(
                        text = elapsedTimeValue,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.previewText
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = announcement.title ?: announcement.content,
                    color = MaterialTheme.colorScheme.previewText,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (announcement.state == AnnouncementState.ERROR) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun AnnouncementHeaderPreview() {
    GedoiseTheme {
        AnnouncementHeader(
            announcement = announcementFixture
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementItemPreview() {
    GedoiseTheme {
        AnnouncementItem(
            announcement = announcementFixture.copy(state = AnnouncementState.ERROR),
            onClick = { }
        )
    }
}