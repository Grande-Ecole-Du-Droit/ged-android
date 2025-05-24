package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.UrlUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toLocalDateTime
import com.upsaclay.common.domain.extensions.toLong
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState

fun Conversation.toLocal() = LocalConversation(
    conversationId = id,
    interlocutorId = interlocutor.id,
    interlocutorFirstName = interlocutor.firstName,
    interlocutorLastName = interlocutor.lastName,
    interlocutorEmail = interlocutor.email,
    interlocutorIsMember = interlocutor.isMember,
    interlocutorSchoolLevel = interlocutor.schoolLevel,
    interlocutorProfilePictureFileName = UrlUtils.getFileNameFromUrl(interlocutor.profilePictureFileName),
    createdAt = createdAt.toLong(),
    conversationState = state.name,
    conversationDeleteTime = deleteTime?.toLong()
)

internal fun Conversation.toRemote(userId: String) = RemoteConversation(
    conversationId = id,
    participants = listOf(userId, interlocutor.id),
    createdAt = createdAt.toTimestamp(),
    deleteBy = mapOf(
        userId to false,
        interlocutor.id to false
    )
)

fun LocalConversation.toConversation(): Conversation {
    val interlocutor = User(
        id = interlocutorId,
        firstName = interlocutorFirstName,
        lastName = interlocutorLastName,
        email = interlocutorEmail,
        schoolLevel = interlocutorSchoolLevel,
        isMember = interlocutorIsMember,
        profilePictureFileName = UrlUtils.formatProfilePictureUrl(interlocutorProfilePictureFileName)
    )

    return Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        createdAt = createdAt.toLocalDateTime(),
        state = ConversationState.valueOf(conversationState),
        deleteTime = conversationDeleteTime?.toLocalDateTime()
    )
}

internal fun RemoteConversation.toConversation(userId: String, interlocutor: User) =
    Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        state = if (deleteBy.containsValue(true)) {
            ConversationState.SOFT_DELETED
        } else {
            ConversationState.CREATED
        },
        createdAt = createdAt.toLocalDateTime(),
        deleteTime = deleteTime?.get(userId)?.toLocalDateTime()
    )

internal fun RemoteConversation.toMap(): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data[ConversationField.CONVERSATION_ID] = conversationId
    data[ConversationField.Remote.PARTICIPANTS] = participants
    data[ConversationField.CREATED_AT] = createdAt
    data[ConversationField.Remote.DELETE_BY] = deleteBy
    deleteTime?.let {
        data[ConversationField.Remote.DELETE_TIME] = it
    }
    return data
}

fun LocalConversationMessage.toConversationMessage() = ConversationMessage(
    conversation = this.toConversation(),
    lastMessage = this.toMessage()
)

private fun LocalConversationMessage.toConversation() = Conversation(
    id = conversationId,
    interlocutor = User(
        id = interlocutorId,
        firstName = interlocutorFirstName,
        lastName = interlocutorLastName,
        email = interlocutorEmail,
        schoolLevel = interlocutorSchoolLevel,
        isMember = interlocutorIsMember,
        profilePictureFileName = UrlUtils.formatProfilePictureUrl(interlocutorProfilePictureFileName)
    ),
    createdAt = createdAt.toLocalDateTime(),
    state = ConversationState.valueOf(conversationState),
    deleteTime = conversationDeleteTime?.toLocalDateTime()
)

private fun LocalConversationMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = messageTimestamp.toLocalDateTime(),
    seen = seen,
    state = MessageState.valueOf(messageState)
)

