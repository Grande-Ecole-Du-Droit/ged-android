package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationsUIFixture
import com.upsaclay.message.domain.entity.ConversationUi

@Composable
fun ConversationFeed(
    conversations: List<ConversationUi>,
    onClick: (ConversationUi) -> Unit,
    onLongClick: (ConversationUi) -> Unit,
    onCreateClick: () -> Unit
) {
    LazyColumn {
        if (conversations.isEmpty()) {
            item { EmptyConversationText(onCreateClick) }
        } else {
            items(conversations.size) { index ->
                val conversation = conversations[index]
                ConversationItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(id = R.string.conversation_screen_conversation_item_tag)),
                    conversation = conversation,
                    onClick = { onClick(conversation) },
                    onLongClick = { onLongClick(conversation) }
                )
            }
        }
    }
}

@Composable
private fun EmptyConversationText(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MaterialTheme.spacing.medium)
            .testTag(stringResource(R.string.conversation_screen_empty_conversation_text_tag)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.start_conversation),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.previewText,
            textAlign = TextAlign.Center
        )

        TextButton(
            contentPadding = PaddingValues(MaterialTheme.spacing.default),
            modifier = Modifier.height(MaterialTheme.spacing.large),
            onClick = onCreateClick
        ) {
            Text(
                text = stringResource(id = R.string.new_conversation),
                fontWeight = FontWeight.Bold
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
private fun ConversationFeedPreview() {
    ConversationFeed(
        conversations = conversationsUIFixture,
        onClick = {},
        onLongClick = {},
        onCreateClick = {}
    )
}