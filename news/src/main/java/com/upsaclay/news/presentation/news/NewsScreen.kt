package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.presentation.announcement.components.CreateAnnouncementFAB
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsDestination(
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: NewsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    NewsScreen(
        user = uiState.user,
        refreshing = uiState.refreshing,
        announcements = uiState.announcements,
        bottomBar = bottomBar,
        snackbarHostState = snackbarHostState,
        onRefresh = viewModel::refreshAnnouncements,
        onAnnouncementClick = onAnnouncementClick,
        onCreateAnnouncementClick = onCreateAnnouncementClick,
        onResendAnnouncementClick = viewModel::recreateAnnouncement,
        onDeleteAnnouncementClick = viewModel::deleteAnnouncement,
        onProfilePictureClick = onProfilePictureClick
    )
}

@Composable
private fun NewsScreen(
    user: User?,
    refreshing: Boolean,
    announcements: List<Announcement>?,
    bottomBar: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onRefresh: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onResendAnnouncementClick: (Announcement) -> Unit,
    onDeleteAnnouncementClick: (Announcement) -> Unit,
    onProfilePictureClick: () -> Unit
) {
    var showAnnouncementBottomSheet by remember { mutableStateOf(false) }
    var showDeleteAnnouncementDialog by remember { mutableStateOf(false) }
    var announcementClicked by remember { mutableStateOf<Announcement?>(null) }

    if (showDeleteAnnouncementDialog) {
        SensibleActionDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
            title = stringResource(id = R.string.delete_announcement_dialog_title),
            text = stringResource(id = R.string.delete_announcement_dialog_text),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteAnnouncementDialog = false
                announcementClicked?.let(onDeleteAnnouncementClick)
            },
            onCancel = { showDeleteAnnouncementDialog = false }
        )
    }

    Scaffold(
        topBar = {
            NewsTopBar(
                userProfilePictureUrl = user?.profilePictureFileName,
                onProfilePictureClick = onProfilePictureClick
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        floatingActionButton = {
            if (user?.isMember == true) {
                CreateAnnouncementFAB(
                    onClick = onCreateAnnouncementClick
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            PullToRefreshComponent(
                onRefresh = onRefresh,
                isRefreshing = refreshing
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
                ) {
                    if (announcements == null) {
                        return@Column
                    }

                    RecentAnnouncementSection(
                        modifier = Modifier.weight(1f),
                        announcements = announcements,
                        onAnnouncementClick = onAnnouncementClick,
                        onNotCreateAnnouncementClick = {
                            announcementClicked = it
                            showAnnouncementBottomSheet = true
                        }
                    )
                }
            }

            if (showAnnouncementBottomSheet) {
                RecentAnnouncementBottomSheet(
                    onDismiss = { showAnnouncementBottomSheet = false },
                    onResendAnnouncementClick = { announcementClicked?.let(onResendAnnouncementClick) },
                    onDeleteAnnouncementClick = {
                        showAnnouncementBottomSheet = false
                        showDeleteAnnouncementDialog = true
                    }
                )
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
private fun NewsScreenPreview() {
    GedoiseTheme {
        NewsScreen(
            user = userFixture,
            refreshing = false,
            announcements = announcementsFixture,
            bottomBar = {},
            onRefresh = {},
            onAnnouncementClick = {},
            onResendAnnouncementClick = {},
            onDeleteAnnouncementClick = {},
            onCreateAnnouncementClick = {},
            onProfilePictureClick = {}
        )
    }
}