package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationsUIFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.toConversation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConversationDestination(
    onConversationClick: (Conversation) -> Unit,
    onCreateConversation: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: ConversationViewModel = koinViewModel()
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
        viewModel.event.collectLatest { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))

                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    ConversationScreen(
        conversations = uiState.conversations,
        onConversationClick = onConversationClick,
        onDeleteConversation = viewModel::deleteConversation,
        onCreateConversation = onCreateConversation,
        snackbarHostState = snackbarHostState,
        bottomBar = bottomBar
    )
}

@Composable
private fun ConversationScreen(
    conversations: List<ConversationUi>?,
    onConversationClick: (Conversation) -> Unit,
    onDeleteConversation: (Conversation) -> Unit,
    onCreateConversation: () -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    bottomBar: @Composable () -> Unit
) {
    var conversationClicked by remember { mutableStateOf<ConversationUi?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteConversationDialog by remember { mutableStateOf(false) }

    if (showDeleteConversationDialog) {
        SensibleActionDialog(
            title = stringResource(id = R.string.delete_conversation_dialog_title),
            text = stringResource(id = R.string.delete_conversation_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteConversationDialog = false
                conversationClicked?.let { onDeleteConversation(it.toConversation()) }
            },
            onCancel = { showDeleteConversationDialog  = false }
        )
    }

    ConversationScaffold(
        onCreateConversation = onCreateConversation,
        snackbarHostState = snackbarHostState,
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            conversations?.let { conversations ->
                ConversationFeed(
                    conversations = conversations,
                    onClick = { onConversationClick(it.toConversation()) },
                    onLongClick = {
                        conversationClicked = it
                        showBottomSheet = true
                    },
                    onCreateClick = onCreateConversation
                )
            }
        }
    }

    if (showBottomSheet) {
        ConversationModalBottomSheet(
            onDismiss = { showBottomSheet = false },
            onDeleteClick = {
                showBottomSheet = false
                showDeleteConversationDialog = true
            }
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun ConversationsScreenPreview() {
    val conversations = conversationsUIFixture.sortedByDescending { it.lastMessage.date }

    GedoiseTheme {
        ConversationScreen(
            conversations = conversations,
            onConversationClick = {},
            onDeleteConversation = {},
            onCreateConversation = {},
            bottomBar = {}
        )
    }
}