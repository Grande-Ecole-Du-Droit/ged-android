package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.MessageDao
import com.upsaclay.message.data.mapper.toMessage
import com.upsaclay.message.data.mapper.toLocal
import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class MessageLocalDataSource(private val messageDao: MessageDao) {
    fun getMessages(conversationId: String): Flow<List<Message>> =
        messageDao.getMessages(conversationId).map { messages ->
            messages.map { it.toMessage() }
        }

    fun getUnreadMessagesByUser(conversationId: String, userId: String): Flow<List<Message>> =
        messageDao.getUnreadMessagesByUser(conversationId, userId).map { messages ->
            messages.map { it.toMessage() }
        }

    suspend fun updateMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.updateMessage(message.toLocal())
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