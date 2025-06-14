package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversationsFlow(): Flow<List<Conversation>>

    suspend fun getConversations(): List<Conversation>

    fun getConversationFlow(interlocutorId: String): Flow<Conversation>

    suspend fun getConversation(interlocutorId: String): Conversation?

    suspend fun fetchRemoteConversations(userId: String): Flow<Conversation>

    suspend fun createConversation(conversation: Conversation, userId: String)

    suspend fun updateLocalConversation(conversation: Conversation)

    suspend fun upsertLocalConversation(conversation: Conversation)

    suspend fun deleteConversation(conversation: Conversation, userId: String)

    suspend fun deleteLocalConversations()
}
