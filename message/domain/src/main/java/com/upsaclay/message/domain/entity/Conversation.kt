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
    fun shouldBeUpdated(): Boolean {
        return state == ConversationState.DRAFT
                || state == ConversationState.DELETED
                || state == ConversationState.ERROR
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Conversation) return false

        return id == other.id
                && interlocutor == other.interlocutor
                && state == other.state
                && deleteTime?.withNano(0) == other.deleteTime?.withNano(0)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + interlocutor.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + (deleteTime?.hashCode() ?: 0)
        return result
    }
}

enum class ConversationState {
    DRAFT,
    CREATING,
    CREATED,
    DELETED,
    ERROR
}