package com.upsaclay.message.data.remote

import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.message.data.mapper.toMap
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

internal class ConversationRemoteDataSource(private val conversationApi: ConversationApi) {
    fun listenConversations(userId: String): Flow<RemoteConversation> =
        conversationApi.listenConversations(userId)

    suspend fun createConversation(conversation: Conversation, userId: String) {
        val data = conversation.toRemote(userId).toMap()
        conversationApi.createConversation(conversation.id, data)
    }

    suspend fun updateConversationDeleteTime(conversationId: String, userId: String, deleteTIme: LocalDateTime) {
        val data = mapOf(
            "${ConversationField.DELETE_TIME}.$userId" to deleteTIme.toTimestamp()
        )
        conversationApi.updateConversation(conversationId, data)
    }
}