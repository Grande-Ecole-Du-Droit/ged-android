package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.ElapsedTime
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.FormatLocalDateTimeUseCase
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationUiFixture
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.messageFixture2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    conversation: ConversationUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val message = conversation.lastMessage
    val elapsedTimeValue = when (val elapsedTime = GetElapsedTimeUseCase.fromLocalDateTime(message.date)) {
        is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)

        is ElapsedTime.Minute -> stringResource(
            com.upsaclay.common.R.string.minute_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Hour -> stringResource(
            com.upsaclay.common.R.string.hour_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Day -> stringResource(
            com.upsaclay.common.R.string.day_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Week -> stringResource(
            com.upsaclay.common.R.string.week_ago_short,
            elapsedTime.value
        )

        is ElapsedTime.Later -> FormatLocalDateTimeUseCase.formatDayMonthYear(elapsedTime.value)
    }

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
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            url = conversation.interlocutor.profilePictureFileName,
            scale = 0.5f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            val text = if (message.state == MessageState.SENT) message.content else stringResource(R.string.sending)
            val isNotSender = message.senderId == conversation.interlocutor.id

            if (isNotSender && !message.seen) {
                UnreadConversationItem(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(stringResource(id = R.string.conversation_screen_unread_conversation_item_tag)),
                    interlocutor = conversation.interlocutor,
                    lastMessage = message,
                    elapsedTime = elapsedTimeValue
                )
            } else {
                ReadConversationItem(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(stringResource(id = R.string.conversation_screen_read_conversation_item_tag)),
                    interlocutor = conversation.interlocutor,
                    lastMessage = message.copy(content = text),
                    elapsedTime = elapsedTimeValue
                )
            }
        }
    }
}

@Composable
private fun ReadConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    lastMessage: Message,
    elapsedTime: String
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = interlocutor.fullName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

            Text(
                text = elapsedTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.previewText
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = lastMessage.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.previewText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun UnreadConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    lastMessage: Message,
    elapsedTime: String
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = interlocutor.fullName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

            Text(
                text = elapsedTime,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = lastMessage.content,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.error)
            .size(10.dp)
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun ReadConversationItemPreview() {
    GedoiseTheme {
        ConversationItem(
            modifier = Modifier.fillMaxWidth(),
            conversation = conversationUiFixture,
            onClick = { },
            onLongClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UnreadConversationItemPreview() {
    GedoiseTheme {
        ConversationItem(
            modifier = Modifier.fillMaxWidth(),
            conversation = conversationUiFixture.copy(
                lastMessage = messageFixture2
            ),
            onClick = { },
            onLongClick = { }
        )
    }
}