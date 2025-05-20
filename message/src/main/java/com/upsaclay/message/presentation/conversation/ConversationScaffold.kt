package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.message.R
import com.upsaclay.message.presentation.conversation.create.CreateConversationFAB

@Composable
fun ConversationScaffold(
    onCreateConversation: () -> Unit,
    snackbarHostState: SnackbarHostState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { TitleTopBar(title = stringResource(R.string.message)) },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(snackbarData = it)
            }
        },
        floatingActionButton = {
            CreateConversationFAB(
                onClick = onCreateConversation
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}