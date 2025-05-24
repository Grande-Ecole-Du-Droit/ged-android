package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getLocalConversationFlow(interlocutorId: String): Flow<Conversation>

    suspend fun getLocalConversation(interlocutorId: String): Conversation?

    suspend fun fetchRemoteConversations(userId: String): Flow<Conversation>

    fun createRemoteConversation(conversation: Conversation, userId: String)

    suspend fun upsertLocalConversation(conversation: Conversation)

    fun unDeleteRemoteConversation(conversation: Conversation, userId: String)

    suspend fun softDeleteConversation(conversation: Conversation, userId: String)

    suspend fun hardDeleteConversation(conversation: Conversation)

    suspend fun deleteLocalConversations()
}
