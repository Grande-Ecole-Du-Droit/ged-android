package com.upsaclay.message.data.repository

import com.upsaclay.common.data.exceptions.handleNetworkException
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
    override fun getLocalMessages(conversationId: String): Flow<List<Message>> =
        messageLocalDataSource.getMessages(conversationId)

    override fun getRemoteMessages(conversationId: String, offsetTime: LocalDateTime?): Flow<List<Message>> =
        messageRemoteDataSource.listenMessages(conversationId, offsetTime)

    override suspend fun createLocalMessage(message: Message) {
        messageLocalDataSource.createMessage(message)
    }

    override suspend fun createRemoteMessage(message: Message) {
        handleNetworkException(
            message = "Failed to create message",
            block = { messageRemoteDataSource.createMessage(message) }
        )
    }

    override suspend fun updateSeenMessage(message: Message) {
        handleNetworkException(
            message = "Failed to update seen message",
            block = {
                messageRemoteDataSource.updateSeenMessage(message)
                messageLocalDataSource.updateMessage(message)
            }
        )
    }

    override suspend fun upsertLocalMessage(message: Message) {
        messageLocalDataSource.upsertMessage(message)
    }

    override suspend fun deleteLocalMessages(conversationId: String) {
        messageLocalDataSource.deleteMessages(conversationId)
    }

    override fun deleteRemoteMessages(conversationId: String) {
        messageRemoteDataSource.deleteMessages(conversationId)
    }

    override suspend fun deleteLocalMessages() {
        messageLocalDataSource.deleteMessages()
    }
}