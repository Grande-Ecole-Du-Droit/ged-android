package com.upsaclay.message.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.upsaclay.message.data.local.dao.MessageDao
import com.upsaclay.message.data.mapper.toLocal
import com.upsaclay.message.data.mapper.toMessage
import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val MESSAGE_LIMIT = 20

internal class MessageLocalDataSource(private val messageDao: MessageDao) {
    fun getMessages(conversationId: String): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(
                pageSize = MESSAGE_LIMIT,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { messageDao.getMessages(conversationId) }
        ).flow.map { messages ->
            messages.map { it.toMessage() }
        }
    }

    suspend fun getUnreadMessagesByUser(conversationId: String, userId: String): List<Message> =
        messageDao.getUnreadMessagesByUser(conversationId, userId).map { it.toMessage() }

    fun getLastMessageFlow(conversationId: String): Flow<Message?> =
        messageDao.getLastMessageFlow(conversationId).map { it?.toMessage() }

    suspend fun getLastMessage(conversationId: String): Message? =
        messageDao.getLastMessage(conversationId)?.toMessage()

    suspend fun insertMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(message.toLocal())
        }
    }

    suspend fun updateMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.updateMessage(message.toLocal())
        }
    }

    suspend fun updateSeenMessages(conversationId: String, userId: String) {
        withContext(Dispatchers.IO) {
            messageDao.updateSeenMessages(conversationId, userId)
        }
    }

    suspend fun upsertMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.upsertMessage(message.toLocal())
        }
    }

    suspend fun deleteMessages(conversationId: String) {
        withContext(Dispatchers.IO) {
            messageDao.deleteMessages(conversationId)
        }
    }

    suspend fun deleteMessages() {
        withContext(Dispatchers.IO) {
            messageDao.deleteAllMessages()
        }
    }
}