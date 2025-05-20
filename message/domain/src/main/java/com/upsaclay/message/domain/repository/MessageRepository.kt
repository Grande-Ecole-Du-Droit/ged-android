package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MessageRepository {
    fun getLocalMessages(conversationId: String): Flow<List<Message>>

    fun getRemoteMessages(conversationId: String, offsetTime: LocalDateTime?): Flow<List<Message>>

    suspend fun createLocalMessage(message: Message)

    suspend fun createRemoteMessage(message: Message)

    suspend fun updateSeenMessage(message: Message)

    suspend fun upsertLocalMessage(message: Message)

    suspend fun deleteLocalMessages()

    suspend fun deleteRemoteMessages(conversationId: String)

    suspend fun deleteLocalMessages(conversationId: String)
}