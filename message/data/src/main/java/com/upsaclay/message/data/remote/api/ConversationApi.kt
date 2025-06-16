package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.flow.Flow

internal interface ConversationApi {
    fun listenConversations(userId: String): Flow<RemoteConversation>

    suspend fun createConversation(conversationId: String, data: Map<String, Any>)

    suspend fun updateConversation(conversationId: String, data: Map<String, Any>)
}