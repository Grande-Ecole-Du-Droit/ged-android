package com.upsaclay.message.domain.entity

import com.upsaclay.common.domain.LocalDateTimeSerializer
import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Conversation(
    val id: String,
    val interlocutor: User,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val state: ConversationState,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deleteTime: LocalDateTime? = null
) {
    val shouldBeCreated: Boolean
        get() = state == ConversationState.DRAFT || state == ConversationState.ERROR
}

enum class ConversationState {
    DRAFT,
    CREATING,
    CREATED,
    DELETING,
    ERROR
}