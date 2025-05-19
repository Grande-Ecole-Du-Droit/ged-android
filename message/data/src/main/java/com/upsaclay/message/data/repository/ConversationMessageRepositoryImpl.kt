package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.ConversationMessageLocalDataSource
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConversationMessageRepositoryImpl(
    conversationMessageLocalDataSource: ConversationMessageLocalDataSource,
    scope: CoroutineScope
): ConversationMessageRepository {
    private val _conversationMessages = conversationMessageLocalDataSource
        .getConversationsMessage()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    override val conversationsMessage: Flow<List<ConversationMessage>> = _conversationMessages
}