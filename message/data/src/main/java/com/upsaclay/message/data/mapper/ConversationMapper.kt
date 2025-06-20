package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.UrlUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
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
    interlocutorProfilePictureFileName = UrlUtils.getFileNameFromUrl(interlocutor.profilePictureUrl),
    createdAt = createdAt.toEpochMilliUTC(),
    conversationState = state.name,
    conversationDeleteTime = deleteTime?.toEpochMilliUTC()
)

internal fun Conversation.toRemote(userId: String) = RemoteConversation(
    conversationId = id,
    participants = listOf(userId, interlocutor.id),
    createdAt = createdAt.toTimestamp(),
    deleteTime = deleteTime?.let { mapOf(userId to it.toTimestamp()) }
)

fun LocalConversation.toConversation(): Conversation {
    val interlocutor = User(
        id = interlocutorId,
        firstName = interlocutorFirstName,
        lastName = interlocutorLastName,
        email = interlocutorEmail,
        schoolLevel = interlocutorSchoolLevel,
        isMember = interlocutorIsMember,
        profilePictureUrl = UrlUtils.formatProfilePictureUrl(interlocutorProfilePictureFileName)
    )

    return Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        createdAt = createdAt.toLocalDateTimeUTC(),
        state = ConversationState.valueOf(conversationState),
        deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
    )
}

internal fun RemoteConversation.toConversation(userId: String, interlocutor: User) =
    Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        state = ConversationState.CREATED,
        createdAt = createdAt.toLocalDateTime(),
        deleteTime = deleteTime?.get(userId)?.toLocalDateTime()
    )

internal fun RemoteConversation.toMap(): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data[ConversationField.CONVERSATION_ID] = conversationId
    data[ConversationField.Remote.PARTICIPANTS] = participants
    data[ConversationField.CREATED_AT] = createdAt
    deleteTime?.let {
        data[ConversationField.DELETE_TIME] = it
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
        profilePictureUrl = UrlUtils.formatProfilePictureUrl(interlocutorProfilePictureFileName)
    ),
    createdAt = createdAt.toLocalDateTimeUTC(),
    state = ConversationState.valueOf(conversationState),
    deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
)

private fun LocalConversationMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = messageTimestamp.toLocalDateTimeUTC(),
    seen = seen,
    state = MessageState.valueOf(messageState)
)

