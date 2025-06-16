package com.upsaclay.message.domain.entity

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val senderId: String,
    val recipientId: String,
    val conversationId: String,
    val content: String,
    val date: LocalDateTime,
    val seen: Boolean = false,
    val state: MessageState,
)

enum class MessageState {
    DRAFT,
    LOADING,
    SENT,
    ERROR
}
