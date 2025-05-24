package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.presentation.announcement.components.AnnouncementItem

@Composable
fun RecentAnnouncementContent(
    modifier: Modifier = Modifier,
    announcements: List<Announcement>,
    onAnnouncementClick: (String) -> Unit,
    onNotCreateAnnouncementClick: (Announcement) -> Unit
) {
    val sortedAnnouncements = announcements.sortedByDescending { it.date }

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = stringResource(id = R.string.recent_announcements),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.medium)
                .testTag(stringResource(id = R.string.news_screen_empty_announcement_text_tag))
        )

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (announcements.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.no_announcement),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.previewText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                }
            } else {
                items(sortedAnnouncements) { announcement ->
                    AnnouncementItem(
                        modifier = Modifier.testTag(stringResource(R.string.news_screen_recent_announcements_tag)),
                        announcement = announcement,
                        onClick = {
                            if (announcement.state != AnnouncementState.PUBLISHED) {
                                onNotCreateAnnouncementClick(announcement)
                            } else {
                                onAnnouncementClick(announcement.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun RecentAnnouncementContentPreview() {
    GedoiseTheme {
        RecentAnnouncementContent(
            announcements = announcementsFixture,
            onAnnouncementClick = { },
            onNotCreateAnnouncementClick = { }
        )
    }
}