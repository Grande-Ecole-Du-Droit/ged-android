package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.flow.Flow

internal interface ConversationApi {
    fun listenConversations(userId: String): Flow<RemoteConversation>

    fun createConversation(conversationId: String, data: Map<String, Any>)

    fun updateConversation(conversationId: String, data: Map<String, Any>)
}