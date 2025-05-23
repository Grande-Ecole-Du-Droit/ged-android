package com.upsaclay.message.domain

import com.upsaclay.common.domain.entity.FcmData
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.domain.entity.FcmMessage
import com.upsaclay.common.domain.entity.FcmNotification
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

fun ConversationMessage.toFcm(user: User) = FcmMessage(
    recipientId = conversation.interlocutor.id,
    notification = FcmNotification(
        title = user.fullName,
        body = lastMessage.content.take(100)
    ),
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = ConversationMessage(
            conversation = conversation.copy(interlocutor = user),
            lastMessage = lastMessage
        )
    )
)
