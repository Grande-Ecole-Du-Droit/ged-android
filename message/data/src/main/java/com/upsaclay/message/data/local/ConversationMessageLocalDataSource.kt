package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.ConversationMessageDao
import com.upsaclay.message.data.mapper.toConversationMessage
import com.upsaclay.message.domain.entity.ConversationMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConversationMessageLocalDataSource(
    private val conversationMessageDao: ConversationMessageDao
) {
    fun getConversationsMessage(): Flow<List<ConversationMessage>> {
        return conversationMessageDao.getConversationsMessage().map { messages ->
            messages.map { it.toConversationMessage() }
        }
    }
}