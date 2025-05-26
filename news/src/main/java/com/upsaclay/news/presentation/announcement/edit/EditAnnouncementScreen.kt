package com.upsaclay.news.presentation.announcement.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.TransparentFocusedTextField
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.hintText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditAnnouncementScreenRoute(
    announcement: Announcement,
    onBackClick: () -> Unit,
    viewModel: EditAnnouncementViewModel = koinViewModel(
        parameters = { parametersOf(announcement) }
    )
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> scope.launch {
                    snackbarHostState.showSnackbar(context.getString(event.messageId))
                }
                is SingleUiEvent.Success -> onBackClick()
            }
        }
    }

    EditAnnouncementScreen(
        title = uiState.title,
        content = uiState.content,
        loading = uiState.loading,
        updateEnabled = uiState.updateEnabled,
        snackbarHostState = snackbarHostState,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBackClick = onBackClick,
        onUpdateAnnouncementClick = viewModel::updateAnnouncement
    )
}

@Composable
private fun EditAnnouncementScreen(
    title: String,
    content: String,
    loading: Boolean,
    updateEnabled: Boolean,
    snackbarHostState: SnackbarHostState,
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String) -> Unit = {},
    onBackClick: () -> Unit,
    onUpdateAnnouncementClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            EditTopBar(
                onCancelClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onBackClick()
                },
                onActionClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onUpdateAnnouncementClick()
                },
                isButtonEnable = updateEnabled && !loading,
                buttonText = stringResource(id = com.upsaclay.common.R.string.save)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    modifier = Modifier.testTag(stringResource(R.string.edit_screen_snackbar_tag)),
                    snackbarData = it
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                TransparentFocusedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_field_entry),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.3f,
                            color = MaterialTheme.colorScheme.hintText
                        )
                    },
                    onValueChange = onTitleChange,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.3f
                    ),
                    enabled = !loading
                )

                TransparentTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = content,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.content_field_entry),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.hintText
                        )
                    },
                    onValueChange = onContentChange,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    enabled = !loading
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
private fun EditAnnouncementScreenPreview() {
    GedoiseTheme {
        Surface {
            EditAnnouncementScreen(
                title = announcementFixture.title ?: "",
                content = announcementFixture.content,
                loading = false,
                updateEnabled = false,
                snackbarHostState = SnackbarHostState(),
                onTitleChange = {},
                onContentChange = {},
                onBackClick = {},
                onUpdateAnnouncementClick = {}
            )
        }
    }
}