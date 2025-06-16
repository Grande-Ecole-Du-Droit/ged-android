package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.FormatLocalDateTimeUseCase
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.domain.longAnnouncementFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState

@Composable
internal fun AnnouncementHeader(
    modifier: Modifier = Modifier,
    announcement: Announcement
) {
    val elapsedTimeValue = getElapsedTimeValue(announcement)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
    ) {
        ProfilePicture(
            url = announcement.author.profilePictureUrl,
            scale = 0.45f
        )

        Text(
            modifier = Modifier.weight(fill = false, weight = 1f),
            text = announcement.author.fullName,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = elapsedTimeValue,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.previewText
        )
    }
}

@Composable
internal fun ShortAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit
) {
    val elapsedTimeValue = getElapsedTimeValue(announcement)
    when (announcement.state) {
        AnnouncementState.PUBLISHED, AnnouncementState.DRAFT -> {
            DefaultShortAnnouncementItem(
                modifier = modifier
                    .clickable(onClick = onClick)
                    .padding(MaterialTheme.spacing.smallMedium),
                announcement = announcement,
                elapsedTimeValue = elapsedTimeValue
            )
        }

        AnnouncementState.PUBLISHING -> {
            PublishingShortAnnouncementItem(
                modifier = modifier,
                announcement = announcement,
                elapsedTimeValue = elapsedTimeValue,
                onClick = onClick
            )
        }

        AnnouncementState.ERROR -> {
            ErrorShortAnnouncementItem(
                modifier = modifier,
                announcement = announcement,
                elapsedTimeValue = elapsedTimeValue,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun DefaultShortAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    elapsedTimeValue: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
    ) {
        ProfilePicture(
            url = announcement.author.profilePictureUrl,
            scale = 0.5f
        )

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
}

@Composable
private fun PublishingShortAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    elapsedTimeValue: String,
    onClick: () -> Unit
) {
    DefaultShortAnnouncementItem(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(MaterialTheme.spacing.smallMedium)
            .alpha(0.5f),
        announcement = announcement,
        elapsedTimeValue = elapsedTimeValue,
    )
}

@Composable
private fun ErrorShortAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    elapsedTimeValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MaterialTheme.spacing.smallMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DefaultShortAnnouncementItem(
            modifier = Modifier.weight(1f),
            announcement = announcement,
            elapsedTimeValue = elapsedTimeValue
        )

        Icon(
            painter = painterResource(com.upsaclay.common.R.drawable.ic_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun getElapsedTimeValue(announcement: Announcement): String {
    return when (val elapsedTime = GetElapsedTimeUseCase.fromLocalDateTime(announcement.date)) {
        is ElapsedTime.Now -> stringResource(com.upsaclay.common.R.string.now, elapsedTime.value)
        is ElapsedTime.Minute -> stringResource(com.upsaclay.common.R.string.minute_ago_short, elapsedTime.value)
        is ElapsedTime.Hour -> stringResource(com.upsaclay.common.R.string.hour_ago_short, elapsedTime.value)
        is ElapsedTime.Day -> stringResource(com.upsaclay.common.R.string.day_ago_short, elapsedTime.value)
        is ElapsedTime.Week -> stringResource(com.upsaclay.common.R.string.week_ago_short, elapsedTime.value)
        is ElapsedTime.Later -> FormatLocalDateTimeUseCase.formatDayMonthYear(elapsedTime.value)
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun FullAnnouncementHeaderPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementHeader(
                announcement = longAnnouncementFixture
            )
        }
    }
}

@Phones
@Composable
private fun DefaultShortAnnouncementItemPreview() {
    GedoiseTheme {
        Surface {
            DefaultShortAnnouncementItem(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.smallMedium)
                    .clickable(onClick = {}),
                announcement = longAnnouncementFixture,
                elapsedTimeValue = "1 min"
            )
        }
    }
}

@Phones
@Composable
private fun PublishingShortAnnouncementItemPreview() {
    GedoiseTheme {
        Surface {
            PublishingShortAnnouncementItem(
                announcement = longAnnouncementFixture,
                elapsedTimeValue = "1 min",
                onClick = { }
            )
        }
    }
}

@Phones
@Composable
private fun ErrorShortAnnouncementItemPreview() {
    GedoiseTheme {
        Surface {
            ErrorShortAnnouncementItem(
                announcement = longAnnouncementFixture.copy(state = AnnouncementState.ERROR),
                elapsedTimeValue = "1 min",
                onClick = { }
            )
        }
    }
}