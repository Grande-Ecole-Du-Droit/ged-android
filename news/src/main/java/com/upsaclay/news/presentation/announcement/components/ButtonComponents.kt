package com.upsaclay.news.presentation.announcement.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@Composable
fun CreateAnnouncementFAB(
    onClick: () -> Unit
) {
    FloatingActionButton (
        onClick = onClick,
        modifier = Modifier
            .testTag(stringResource(id = com.upsaclay.news.R.string.news_screen_create_announcement_button_tag)),
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Icon(
            Icons.Default.Add,
            stringResource(id = com.upsaclay.news.R.string.new_announcement)
        )
    }
}

@Phones
@Composable
private fun CreateAnnouncementFABPreview() {
    GedoiseTheme {
        CreateAnnouncementFAB(onClick = {})
    }
}