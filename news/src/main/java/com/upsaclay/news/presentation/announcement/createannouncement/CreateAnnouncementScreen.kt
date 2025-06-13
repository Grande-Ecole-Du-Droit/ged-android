package com.upsaclay.news.presentation.announcement.createannouncement

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import com.upsaclay.news.R
import com.upsaclay.news.presentation.announcement.components.AnnouncementInput
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateAnnouncementDestination(
    onBackClick: () -> Unit,
    viewModel: CreateAnnouncementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateAnnouncementScreen(
        title = uiState.title,
        content = uiState.content,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBackClick = onBackClick,
        onCreateAnnouncementClick = {
            viewModel.createAnnouncement()
            onBackClick()
        }
    )
}

@Composable
private fun CreateAnnouncementScreen(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateAnnouncementClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            EditTopBar(
                modifier = Modifier.fillMaxWidth(),
                buttonText = stringResource(id = com.upsaclay.common.R.string.publish),
                title = stringResource(id = R.string.new_announcement),
                onCancelClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onBackClick()
                },
                onActionClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onCreateAnnouncementClick()
                },
                isButtonEnable = content.isNotBlank()
            )
        }
    ) { contentPadding ->
        AnnouncementInput(
            modifier = Modifier.mediumPadding(contentPadding),
            title = title,
            content = content,
            onTitleChange = onTitleChange,
            onContentChange = onContentChange
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateAnnouncementScreenPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        CreateAnnouncementScreen(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            onBackClick = {},
            onCreateAnnouncementClick = {}
        )
    }
}