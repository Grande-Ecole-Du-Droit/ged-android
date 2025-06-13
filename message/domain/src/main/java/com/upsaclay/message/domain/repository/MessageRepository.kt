package com.upsaclay.message.domain.repository

import androidx.paging.PagingData
import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MessageRepository {
    fun getPagingMessages(conversationId: String): Flow<PagingData<Message>>

    fun getLastMessage(conversationId: String): Flow<Message>

    fun fetchRemoteMessages(conversationId: String, offsetTime: LocalDateTime?): Flow<Message>

    fun getUnreadMessagesByUser(conversationId: String, userId: String): Flow<List<Message>>

    suspend fun createMessage(message: Message)

    suspend fun createRemoteMessage(message: Message)

    suspend fun updateSeenMessage(message: Message)

    suspend fun upsertLocalMessage(message: Message)

    suspend fun updateLocalMessage(message: Message)

    suspend fun deleteLocalMessages()

    suspend fun deleteLocalMessages(conversationId: String)
}