package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    val conversations: Flow<List<Conversation>>

    suspend fun createConversation(conversation: Conversation)
}