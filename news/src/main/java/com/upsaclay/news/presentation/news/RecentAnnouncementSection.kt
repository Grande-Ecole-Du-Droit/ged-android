package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.upsaclay.news.presentation.announcement.components.ShortAnnouncementItem

@Composable
fun RecentAnnouncementSection(
    modifier: Modifier = Modifier,
    announcements: List<Announcement>,
    onAnnouncementClick: (String) -> Unit,
    onUncreatedAnnouncementClick: (Announcement) -> Unit
) {
    Column(
        modifier = modifier,
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
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (announcements.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.medium),
                        text = stringResource(id = R.string.no_announcement),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.previewText,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(announcements) { announcement ->
                    ShortAnnouncementItem(
                        modifier = Modifier.testTag(stringResource(R.string.news_screen_recent_announcements_tag)),
                        announcement = announcement,
                        onClick = {
                            if (announcement.state == AnnouncementState.PUBLISHED) {
                                onAnnouncementClick(announcement.id)
                            } else {
                                onUncreatedAnnouncementClick(announcement)
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
        Surface {
            RecentAnnouncementSection(
                announcements = announcementsFixture,
                onAnnouncementClick = { },
                onUncreatedAnnouncementClick = { }
            )
        }
    }
}