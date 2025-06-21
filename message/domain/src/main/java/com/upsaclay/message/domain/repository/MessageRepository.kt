package com.upsaclay.message.domain.repository

import androidx.paging.PagingData
import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MessageRepository {
    fun getPagingMessages(conversationId: String): Flow<PagingData<Message>>

    fun getLastMessageFlow(conversationId: String): Flow<Message?>

    suspend fun getLastMessage(conversationId: String): Message?

    suspend fun getUnsentMessages(): List<Message>

    fun fetchRemoteMessages(conversationId: String, interlocutorId: String, offsetTime: LocalDateTime?): Flow<Message>

    suspend fun createMessage(message: Message)

    suspend fun createRemoteMessage(message: Message)

    suspend fun updateLocalMessage(message: Message)

    suspend fun updateSeenMessages(conversationId: String, userId: String)

    suspend fun updateSeenMessage(message: Message)

    suspend fun upsertLocalMessage(message: Message)

    suspend fun deleteLocalMessage(message: Message)

    suspend fun deleteLocalMessages()

    suspend fun deleteLocalMessages(conversationId: String)
}