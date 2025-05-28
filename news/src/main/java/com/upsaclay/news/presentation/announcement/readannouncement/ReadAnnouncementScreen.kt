package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.entity.Announcement
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadAnnouncementScreenRoute(
    announcementId: String,
    onBackClick: () -> Unit,
    onEditClick: (Announcement) -> Unit,
    viewModel: ReadAnnouncementViewModel = koinViewModel(
        parameters = { parametersOf(announcementId) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.singleUiEvent.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.messageId)
                    )
                }

                is SingleUiEvent.Success -> onBackClick()
            }
        }
    }

    if (uiState.user != null && uiState.announcement != null) {
        ReadAnnouncementScreen(
            user = uiState.user!!,
            announcement = uiState.announcement!!,
            loading = uiState.loading,
            snackbarHostState = snackbarHostState,
            onDeleteAnnouncement = viewModel::deleteAnnouncement,
            onBackClick = onBackClick,
            onEditClick = onEditClick
        )
    }
}

@Composable
fun ReadAnnouncementScreen(
    user: User,
    announcement: Announcement,
    loading: Boolean = false,
    snackbarHostState: SnackbarHostState,
    onDeleteAnnouncement: () -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (Announcement) -> Unit
) {
    var showDeleteAnnouncementDialog by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }

    if (showDeleteAnnouncementDialog) {
        SensibleActionDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
            title = stringResource(id = R.string.delete_announcement_dialog_title),
            text = stringResource(id = R.string.delete_announcement_dialog_text),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteAnnouncementDialog = false
                onDeleteAnnouncement()
            },
            onCancel = { showDeleteAnnouncementDialog = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.announcement)
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
            ) {
               Snackbar(
                   snackbarData = it,
                   modifier = Modifier.testTag(stringResource(id = R.string.read_screen_snackbar_tag))
               )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            ReadAnnouncementTopSection(
                user = user,
                announcement = announcement,
                onEditIconClick = { showBottomSheet = true }
            )

            announcement.title?.let {
                Text(
                    modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_title_tag)),
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.3f
                )
            }

            Text(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_content_tag)),
                text = announcement.content,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (showBottomSheet) {
            ReadAnnouncementBottomSheet(
                onEditClick = {
                    showBottomSheet = false
                    onEditClick(announcement)
                },
                onDeleteClick = {
                    showBottomSheet = false
                    showDeleteAnnouncementDialog = true
                },
                onDismiss = { showBottomSheet = false }
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
private fun NonEditableAnnouncementScreenPreview() {
    GedoiseTheme {
        Surface {
            ReadAnnouncementScreen(
                user = userFixture2,
                announcement = announcementFixture,
                snackbarHostState = SnackbarHostState(),
                onDeleteAnnouncement = {},
                onBackClick = {},
                onEditClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun EditableAnnouncementScreenPreview() {
    GedoiseTheme {
        Surface {
            ReadAnnouncementScreen(
                user = announcementFixture.author,
                announcement = announcementFixture,
                snackbarHostState = SnackbarHostState(),
                onDeleteAnnouncement = {},
                onBackClick = {},
                onEditClick = {}
            )
        }
    }
}