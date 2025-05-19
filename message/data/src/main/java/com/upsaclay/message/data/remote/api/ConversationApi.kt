package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.flow.Flow

internal interface ConversationApi {
    fun listenConversations(userId: String): Flow<RemoteConversation>

    suspend fun getConversation(conversationId: String): RemoteConversation?

    suspend fun createConversation(remoteConversation: RemoteConversation)

    suspend fun updateConversation(conversationId: String, data: Map<String, Any>)

    suspend fun hardDeleteConversation(conversationId: String)
}