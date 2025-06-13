package com.upsaclay.message.data.remote

import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.message.data.mapper.toMessage
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.remote.api.MessageApi
import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class MessageRemoteDataSource(private val messageApi: MessageApi) {
    fun listenMessages(
        conversationId: String,
        interlocutorId: String,
        offsetTime: LocalDateTime?
    ): Flow<Message> {
        return messageApi.listenMessages(conversationId, interlocutorId, offsetTime?.toTimestamp())
            .map { it.toMessage() }
    }

    suspend fun createMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageApi.createMessage(message.toRemote())
        }
    }

    suspend fun updateSeenMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageApi.updateSeenMessage(message.toRemote())
        }
    }
}