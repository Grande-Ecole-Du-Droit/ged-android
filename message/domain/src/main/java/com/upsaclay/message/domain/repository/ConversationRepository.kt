package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getLocalConversationFlow(interlocutorId: String): Flow<Conversation>

    suspend fun getLocalConversation(interlocutorId: String): Conversation?

    suspend fun getRemoteConversationState(conversationId: String, interlocutorId: String): ConversationState?

    suspend fun fetchRemoteConversations(userId: String): Flow<Conversation>

    suspend fun createRemoteConversation(conversation: Conversation, userId: String)

    suspend fun upsertLocalConversation(conversation: Conversation)

    suspend fun unDeleteRemoteConversation(conversation: Conversation, userId: String)

    suspend fun softDeleteConversation(conversation: Conversation, userId: String)

    suspend fun hardDeleteConversation(conversationId: String)

    suspend fun deleteLocalConversations()
}
