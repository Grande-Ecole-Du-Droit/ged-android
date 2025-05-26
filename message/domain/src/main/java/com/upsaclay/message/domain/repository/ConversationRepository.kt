package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getLocalConversationFlow(interlocutorId: String): Flow<Conversation>

    suspend fun getLocalConversation(interlocutorId: String): Conversation?

    suspend fun getRemoteConversations(userId: String): Flow<Conversation>

    suspend fun createConversation(conversation: Conversation, userId: String)

    suspend fun upsertLocalConversation(conversation: Conversation)

    suspend fun deleteConversation(conversation: Conversation, userId: String)

    suspend fun deleteLocalConversations()
}
