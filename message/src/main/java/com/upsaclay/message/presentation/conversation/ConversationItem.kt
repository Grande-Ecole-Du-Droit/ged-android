package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.messageFixture

@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    conversationUi: ConversationUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val lastMessage = conversationUi.lastMessage
    val interlocutor = conversationUi.interlocutor
    val elapsedTimeValue = getElapsedTimeValue(lastMessage.date)
    val text = when(lastMessage.state) {
        MessageState.SENT, MessageState.ERROR -> lastMessage.content
        MessageState.SENDING -> stringResource(R.string.sending)
    }
    val isNotSender = lastMessage.senderId == interlocutor.id

    SwitchConversationItem(
        modifier = modifier,
        interlocutor = conversationUi.interlocutor,
        conversationState = conversationUi.state,
        text = text,
        isUnread = isNotSender && !lastMessage.seen,
        elapsedTime = elapsedTimeValue,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

@Composable
private fun SwitchConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    conversationState: ConversationState,
    isUnread: Boolean,
    text: String,
    elapsedTime: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    ConversationItemStructure(
        modifier = modifier,
        interlocutor = interlocutor,
        onClick = onClick,
        onLongClick = onLongClick
    ) { innerModifier ->
        if (conversationState == ConversationState.CREATING || conversationState == ConversationState.DELETING) {
            ReadConversationItemContent(
                modifier = innerModifier
                    .alpha(0.5f)
                    .testTag(stringResource(id = R.string.conversation_screen_read_conversation_item_tag)),
                interlocutorName = interlocutor.fullName,
                text = text,
                elapsedTime = elapsedTime
            )
        } else {
            if (isUnread) {
                UnreadConversationItemContent(
                    modifier = innerModifier
                        .testTag(stringResource(id = R.string.conversation_screen_unread_conversation_item_tag)),
                    interlocutorName = interlocutor.fullName,
                    text = text,
                    elapsedTime = elapsedTime
                )
            } else {
                ReadConversationItemContent(
                    modifier = innerModifier
                        .testTag(stringResource(id = R.string.conversation_screen_read_conversation_item_tag)),
                    interlocutorName = interlocutor.fullName,
                    text = text,
                    elapsedTime = elapsedTime
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConversationItemStructure(
    modifier: Modifier = Modifier,
    interlocutor: User,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    conversationItemContent: @Composable RowScope.(Modifier) -> Unit
) {
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.smallMedium
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        ProfilePicture(
            url = interlocutor.profilePictureUrl,
            scale = 0.5f
        )

        conversationItemContent(Modifier.weight(1f))
    }
}

@Composable
private fun ReadConversationItemContent(
    modifier: Modifier = Modifier,
    interlocutorName: String,
    text: String,
    elapsedTime: String
) {
    DefaultConversationItemContent(
        modifier = modifier,
        interlocutorName = interlocutorName,
        text = text,
        elapsedTime = elapsedTime,
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun UnreadConversationItemContent(
    modifier: Modifier = Modifier,
    interlocutorName: String,
    text: String,
    elapsedTime: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
    ) {
        DefaultConversationItemContent(
            modifier = Modifier.weight(1f),
            interlocutorName = interlocutorName,
            text = text,
            elapsedTime = elapsedTime,
            fontWeight = FontWeight.SemiBold,
            textColor = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error)
                .size(10.dp)
        )
    }
}

@Composable
private fun DefaultConversationItemContent(
    modifier: Modifier = Modifier,
    interlocutorName: String,
    text: String,
    elapsedTime: String,
    fontWeight: FontWeight,
    textColor: Color = MaterialTheme.colorScheme.previewText
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.veryExtraSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
        ) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = interlocutorName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = fontWeight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = elapsedTime,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = fontWeight,
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            fontWeight = fontWeight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
private fun ReadConversationItemPreview() {
    GedoiseTheme {
        Surface {
            SwitchConversationItem(
                interlocutor = userFixture,
                conversationState = ConversationState.CREATED,
                isUnread = false,
                text = messageFixture.content,
                elapsedTime = "1 min",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun UnreadConversationItemPreview() {
    GedoiseTheme {
        Surface {
            SwitchConversationItem(
                interlocutor = userFixture,
                conversationState = ConversationState.CREATED,
                isUnread = true,
                text = messageFixture.content,
                elapsedTime = "1 min",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun SendingConversationItemPreview() {
    GedoiseTheme {
        Surface {
            SwitchConversationItem(
                interlocutor = userFixture,
                conversationState = ConversationState.CREATING,
                isUnread = false,
                text = messageFixture.content,
                elapsedTime = "1 min",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}


