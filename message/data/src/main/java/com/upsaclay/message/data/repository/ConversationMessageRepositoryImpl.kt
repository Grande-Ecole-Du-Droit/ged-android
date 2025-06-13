package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.ConversationMessageLocalDataSource
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import kotlinx.coroutines.flow.Flow

class ConversationMessageRepositoryImpl(
    conversationMessageLocalDataSource: ConversationMessageLocalDataSource
): ConversationMessageRepository {
    override val conversationsMessage: Flow<List<ConversationMessage>> =
        conversationMessageLocalDataSource.getConversationsMessage()
}