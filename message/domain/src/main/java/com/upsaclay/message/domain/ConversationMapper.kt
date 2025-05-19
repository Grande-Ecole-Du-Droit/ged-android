package com.upsaclay.message.domain

import com.upsaclay.common.domain.entity.FCMData
import com.upsaclay.common.domain.entity.FCMDataType
import com.upsaclay.common.domain.entity.FCMMessage
import com.upsaclay.common.domain.entity.FCMNotification
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationUi

fun ConversationUi.toConversation() = Conversation(
    id = id,
    interlocutor = interlocutor,
    createdAt = createdAt,
    state = state
)

fun ConversationMessage.toConversationUI() = ConversationUi(
    id = conversation.id,
    interlocutor = conversation.interlocutor,
    lastMessage = lastMessage,
    createdAt = conversation.createdAt,
    state = conversation.state
)

fun ConversationMessage.toFcm(user: User) = FCMMessage(
    recipientId = conversation.interlocutor.id,
    notification = FCMNotification(
        title = user.fullName,
        body = lastMessage.content.take(100)
    ),
    data = FCMData(
        type = FCMDataType.MESSAGE,
        value = ConversationMessage(
            conversation = conversation.copy(interlocutor = user),
            lastMessage = lastMessage
        )
    )
)
