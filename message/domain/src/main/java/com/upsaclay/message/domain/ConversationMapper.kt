package com.upsaclay.message.domain

import com.upsaclay.common.domain.entity.FcmAndroid
import com.upsaclay.common.domain.entity.FcmAndroidNotification
import com.upsaclay.common.domain.entity.FcmData
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.domain.entity.FcmMessage
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.usecase.MESSAGE_CHANNEL_NOTIFICATION_ID

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
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = ConversationMessage(
            conversation = conversation.copy(interlocutor = user),
            lastMessage = lastMessage
        )
    ),
    android = FcmAndroid(
        notification = FcmAndroidNotification(
            channelId = MESSAGE_CHANNEL_NOTIFICATION_ID
        )
    )
)
