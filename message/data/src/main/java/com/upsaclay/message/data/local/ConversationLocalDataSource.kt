package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.mapper.toConversation
import com.upsaclay.message.data.mapper.toLocal
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ConversationLocalDataSource(
    private val conversationDao: ConversationDao
) {
    fun getFlowLocalConversation(interlocutorId: String): Flow<Conversation?> =
        conversationDao.getFlowConversation(interlocutorId).map { it?.toConversation() }

    suspend fun getConversation(interlocutorId: String): Conversation? =
        conversationDao.getConversation(interlocutorId)?.toConversation()

    suspend fun createConversation(conversation: Conversation) {
        conversationDao.insertConversation(conversation.toLocal())
    }

    suspend fun upsertConversation(conversation: Conversation) {
        conversationDao.upsertConversation(conversation.toLocal())
    }

    suspend fun deleteConversation(conversation: Conversation) {
        conversationDao.deleteConversation(conversation.toLocal())
    }

    suspend fun deleteConversations() {
        conversationDao.deleteConversations()
    }
}