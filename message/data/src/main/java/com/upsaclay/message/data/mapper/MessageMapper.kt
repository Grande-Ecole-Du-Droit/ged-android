package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.extensions.toLocalDateTime
import com.upsaclay.common.domain.extensions.toLong
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState

internal fun RemoteMessage.toDomain() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = timestamp.toLocalDateTime(),
    seen = seen,
    state = MessageState.SENT
)

fun LocalMessage.toDomain() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = messageTimestamp.toLocalDateTime(),
    seen = seen,
    state = MessageState.valueOf(state)
)

fun Message.toLocal() = LocalMessage(
    messageId = id,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    messageTimestamp = date.toLong(),
    seen = seen,
    state = state.name
)

internal fun Message.toRemote() = RemoteMessage(
    messageId = id,
    conversationId = conversationId,
    senderId = senderId,
    recipientId = recipientId,
    content = content,
    timestamp = date.toTimestamp(),
    seen = seen
)