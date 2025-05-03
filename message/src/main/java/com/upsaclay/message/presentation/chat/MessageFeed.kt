package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usecase.FormatLocalDateTimeUseCase
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.messagesFixture
import com.upsaclay.message.presentation.chat.ChatViewModel.MessageEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.Duration

@Composable
internal fun MessageFeed(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    interlocutor: User,
    newMessageEvent: MessageEvent.NewMessage?
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showNewMessageIndicator by remember { mutableStateOf(false) }
    val isAtBottom = remember {
        derivedStateOf { scrollState.firstVisibleItemIndex == 0 }
    }

    LaunchedEffect(newMessageEvent) {
        newMessageEvent?.let { event ->
            when {
                scrollState.firstVisibleItemIndex <= 1 &&
                        scrollState.layoutInfo.visibleItemsInfo.size < messages.size ->
                    scrollState.animateScrollToItem(0)

                scrollState.firstVisibleItemIndex > 1 && event.message.senderId == interlocutor.id ->
                    showNewMessageIndicator = true
            }
        }
    }

    if (isAtBottom.value) {
        showNewMessageIndicator = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.testTag(stringResource(R.string.chat_screen_lazy_column_item_tag)),
            reverseLayout = true,
            state = scrollState
        ) {
            itemsIndexed(messages) { index, message ->
                val isSender = message.senderId != interlocutor.id
                val isFirstMessage = index == messages.size - 1
                val isLastMessage = index == 0
                val previousMessage = if (index + 1 < messages.size) messages[index + 1] else null

                val previousSenderId = previousMessage?.senderId ?: ""
                val sameSender = previousSenderId == message.senderId
                val showSeenMessage = isLastMessage && isSender && message.seen?.value == true

                val sameTime = previousMessage?.let {
                    Duration.between(it.date, message.date).toMinutes() < 2L
                } ?: false

                val sameDay = previousMessage?.let {
                    Duration.between(it.date, message.date).toDays() < 2L
                } ?: false

                val displayProfilePicture = !sameTime || isFirstMessage || !sameSender

                if (isSender) {
                    SentMessageItem(
                        modifier = Modifier
                            .testTag(stringResource(R.string.chat_screen_send_message_item_tag) + index),
                        message = message,
                        showSeen = showSeenMessage
                    )
                } else {
                    ReceiveMessageItem(
                        modifier = Modifier
                            .testTag(stringResource(R.string.chat_screen_receive_message_item_tag) + index),
                        message = message,
                        displayProfilePicture = displayProfilePicture,
                        profilePictureUrl = interlocutor.profilePictureUrl
                    )
                }

                if (isFirstMessage || !sameDay) {
                    val topPadding = if (isFirstMessage) {
                        MaterialTheme.spacing.default
                    } else {
                        MaterialTheme.spacing.mediumLarge
                    }

                    Text(
                        modifier = Modifier
                            .padding(top = topPadding, bottom = MaterialTheme.spacing.mediumLarge)
                            .fillMaxWidth(),
                        text = FormatLocalDateTimeUseCase.formatDayMonthYear(message.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.previewText,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Spacer(modifier = Modifier.height(messagePadding(sameSender, sameTime)))
                }
            }
        }

        if (showNewMessageIndicator) {
            NewMessageIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .testTag(stringResource(R.string.chat_screen_message_indicator_tag)),
                onClick = { scope.launch { scrollState.animateScrollToItem(0) } }
            )
        }
    }
}

@Composable
private fun messagePadding(sameSender: Boolean, sameTime: Boolean): Dp =
    if (sameSender && sameTime) 1.dp else MaterialTheme.spacing.small

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun MessageFeedPreview() {
    GedoiseTheme {
        Surface {
            MessageFeed(
                modifier = Modifier
                    .fillMaxSize()
                    .mediumPadding(),
                messages = messagesFixture,
                interlocutor = conversationFixture.interlocutor,
                newMessageEvent = null
            )
        }
    }
}