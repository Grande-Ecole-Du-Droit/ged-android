package com.upsaclay.message.data.repository

import androidx.paging.PagingData
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
    override fun getPagingMessages(conversationId: String): Flow<PagingData<Message>> =
        messageLocalDataSource.getMessages(conversationId)

    override fun getLastMessage(conversationId: String): Flow<Message> =
        messageLocalDataSource.getLastMessage(conversationId)

    override fun fetchRemoteMessages(conversationId: String, offsetTime: LocalDateTime?): Flow<Message> =
        messageRemoteDataSource.listenMessages(conversationId, offsetTime)

    override fun getUnreadMessagesByUser(conversationId: String, userId: String): Flow<List<Message>> =
        messageLocalDataSource.getUnreadMessagesByUser(conversationId, userId)

    override suspend fun createMessage(message: Message) {
        handleNetworkException(
            message = "Failed to create message",
            block = {
                messageLocalDataSource.upsertMessage(message)
                messageRemoteDataSource.createMessage(message)
            }
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

    override suspend fun deleteLocalMessages() {
        messageLocalDataSource.deleteMessages()
    }
}