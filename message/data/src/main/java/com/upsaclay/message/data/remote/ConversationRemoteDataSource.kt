package com.upsaclay.message.data.remote

import com.google.firebase.Timestamp
import com.upsaclay.message.data.mapper.toMap
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class ConversationRemoteDataSource(private val conversationApi: ConversationApi) {
    fun listenConversations(userId: String): Flow<RemoteConversation> = conversationApi.listenConversations(userId)

    fun createConversation(conversation: Conversation, userId: String) {
        val data = conversation.toRemote(userId).toMap()
        conversationApi.createConversation(conversation.id, data)
    }

    fun unDeleteConversation(conversation: Conversation, userId: String) {
        val data = mapOf(
            "${ConversationField.Remote.DELETE_BY}.$userId" to false,
            "${ConversationField.Remote.DELETE_BY}.${conversation.interlocutor.id}" to false,
        )
        conversationApi.updateConversation(conversation.id, data)
    }

    suspend fun softDeleteConversation(conversationId: String, userId: String, timestamp: Timestamp) {
        val data = mapOf(
            "${ConversationField.Remote.DELETE_BY}.$userId" to true,
            "${ConversationField.Remote.DELETE_TIME}.$userId" to timestamp
        )
        withContext(Dispatchers.IO) {
            conversationApi.updateConversation(conversationId, data)
        }
    }

    fun hardDeleteConversation(conversationId: String) {
        conversationApi.hardDeleteConversation(conversationId)
    }
}