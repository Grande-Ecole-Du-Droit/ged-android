package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.messagesFixture
import com.upsaclay.message.domain.toConversation
import com.upsaclay.message.presentation.chat.ChatViewModel.MessageEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.lang.Error

@Composable
fun ChatDestination(
    conversation: Conversation,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = koinViewModel {
        parametersOf(conversation)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    var newMessageEvent by remember { mutableStateOf<MessageEvent.NewMessage?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is MessageEvent.NewMessage -> newMessageEvent = event

                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    ChatScreen(
        conversation = conversation,
        messages = viewModel.messages,
        text = uiState.text,
        snackbarHostState = snackbarHostState,
        newMessageEvent = newMessageEvent,
        onTextChange = viewModel::onTextChange,
        onSendMessage = viewModel::sendMessage,
        onResendMessageClick = viewModel::resendErrorMessage,
        onDeleteMessageClick = viewModel::deleteErrorMessage,
        onBackClick = onBackClick,
    )
}

@Composable
private fun ChatScreen(
    conversation: Conversation,
    messages: Flow<PagingData<Message>>,
    text: String,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    newMessageEvent: MessageEvent.NewMessage?,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onResendMessageClick: (Message) -> Unit,
    onDeleteMessageClick: (Message) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var messageClicked: Message? by remember { mutableStateOf(null) }
    var showDeleteMessageDialog by remember { mutableStateOf(false) }

    if (showDeleteMessageDialog) {
        SensibleActionDialog(
            title = stringResource(id = R.string.delete_message_dialog_title),
            text = stringResource(id = R.string.delete_message_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteMessageDialog = false
                messageClicked?.let(onDeleteMessageClick)
            },
            onCancel = { showDeleteMessageDialog  = false }
        )
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                navController = rememberNavController(),
                interlocutor = conversation.interlocutor,
                onClickBack = {
                    keyboardController?.hide()
                    onBackClick()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.veryExtraLarge),
                hostState = snackbarHostState
            ) {
                Snackbar(it)
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .mediumPadding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            MessageFeed(
                modifier = Modifier.weight(1f),
                messages = messages,
                interlocutor = conversation.interlocutor,
                newMessageEvent = newMessageEvent,
                onClickSendMessage = {
                    if (it.state == MessageState.ERROR) {
                        messageClicked = it
                        showBottomSheet = true
                    }
                }
            )

            MessageInput(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = onTextChange,
                onSendClick = onSendMessage
            )
        }

        if (showBottomSheet) {
            ChatBottomSheet(
                onDismiss = { showBottomSheet = false },
                onResendMessageClick = { messageClicked?.let(onResendMessageClick) },
                onDeleteMessageClick = { showDeleteMessageDialog = true }
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
private fun ChatScreenPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        ChatScreen(
            conversation = conversationFixture,
            messages = flowOf(PagingData.from(messagesFixture)),
            text = text,
            newMessageEvent = null,
            onTextChange = { text = it },
            onSendMessage = {},
            onDeleteMessageClick = {},
            onResendMessageClick = {},
            onBackClick = {}
        )
    }
}