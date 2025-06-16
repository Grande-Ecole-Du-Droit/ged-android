package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState

internal fun RemoteMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = timestamp.toLocalDateTime(),
    seen = seen,
    state = MessageState.SENT
)

fun LocalMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = messageTimestamp.toLocalDateTimeUTC(),
    seen = seen,
    state = MessageState.valueOf(state)
)

fun Message.toLocal() = LocalMessage(
    messageId = id,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    messageTimestamp = date.toEpochMilliUTC(),
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