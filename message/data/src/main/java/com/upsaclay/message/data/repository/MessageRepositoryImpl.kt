package com.upsaclay.message.data.repository

import androidx.paging.PagingData
import com.upsaclay.message.data.local.MessageLocalDataSource
import com.upsaclay.message.data.remote.MessageRemoteDataSource
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

internal class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
): MessageRepository {
    override fun getPagingMessages(conversationId: String): Flow<PagingData<Message>> =
        messageLocalDataSource.getMessages(conversationId)

    override fun getLastMessageFlow(conversationId: String): Flow<Message?> =
        messageLocalDataSource.getLastMessageFlow(conversationId)

    override suspend fun getLastMessage(conversationId: String): Message? =
        messageLocalDataSource.getLastMessage(conversationId)

    override fun fetchRemoteMessages(conversationId: String, interlocutorId: String, offsetTime: LocalDateTime?): Flow<Message> =
        messageRemoteDataSource.listenMessages(conversationId, interlocutorId, offsetTime)

    override suspend fun createMessage(message: Message) {
        messageLocalDataSource.insertMessage(message)
        messageRemoteDataSource.createMessage(message)
    }

    override suspend fun updateSeenMessages(conversationId: String, userId: String) {
        messageLocalDataSource.getUnreadMessagesByUser(conversationId, userId).forEach { message ->
            messageRemoteDataSource.updateSeenMessage(message.copy(seen = true))
        }
        messageLocalDataSource.updateSeenMessages(conversationId, userId)
    }

    override suspend fun updateSeenMessage(message: Message) {
        messageLocalDataSource.updateMessage(message.copy(seen = true))
        messageRemoteDataSource.updateSeenMessage(message.copy(seen = true))
    }

    override suspend fun updateLocalMessage(message: Message) {
        messageLocalDataSource.updateMessage(message)
    }

    override suspend fun upsertLocalMessage(message: Message) {
        messageLocalDataSource.upsertMessage(message)
    }

    override suspend fun deleteLocalMessages(conversationId: String) {
        messageLocalDataSource.deleteMessages(conversationId)
    }

    override suspend fun deleteLocalMessages() {
        messageLocalDataSource.deleteMessages()
    }
}