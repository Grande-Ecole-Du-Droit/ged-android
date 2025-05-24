package com.upsaclay.news.presentation.announcement.read

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.presentation.announcement.components.AnnouncementHeader

@Composable
fun ReadAnnouncementHeader(
    currentUser: User,
    announcement: Announcement,
    onEditIconClick: () -> Unit
) {
    if (currentUser.isMember && announcement.author.id == currentUser.id) {
        EditableTopSection(
            announcement = announcement,
            onEditIconClick = onEditIconClick
        )
    } else {
        AnnouncementHeader(announcement = announcement)
    }
}

@Composable
private fun EditableTopSection(
    announcement: Announcement,
    onEditIconClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AnnouncementHeader(
            announcement = announcement,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        IconButton(
            onClick = onEditIconClick,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .testTag(stringResource(id = R.string.read_screen_option_button_tag))
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.MoreVert,
                tint = Color.Gray,
                contentDescription = stringResource(id = R.string.announcement_item_more_vert_description)
            )
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
private fun ReadAnnouncementHeaderPreview() {
    GedoiseTheme {
        Surface {
            ReadAnnouncementHeader(
                currentUser = userFixture2,
                announcement = announcementFixture,
                onEditIconClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun ReadAnnouncementHeaderEditablePreview() {
    GedoiseTheme(darkTheme = true) {
        Surface {
            ReadAnnouncementHeader(
                currentUser = userFixture,
                announcement = announcementFixture,
                onEditIconClick = {}
            )
        }
    }
}