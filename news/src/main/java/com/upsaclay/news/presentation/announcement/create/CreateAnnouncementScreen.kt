package com.upsaclay.news.presentation.announcement.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.components.TransparentFocusedTextField
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.hintText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateAnnouncementScreenRoute(
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
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.3f
                    )
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
                    textStyle = MaterialTheme.typography.bodyLarge
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