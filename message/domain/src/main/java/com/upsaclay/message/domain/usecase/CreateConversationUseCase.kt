package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateConversationUseCase(
    private val conversationRepository: ConversationRepository
) {
    fun generateNewConversation(userId: String, interlocutor: User): Conversation {
        val conversationId = if (userId > interlocutor.id) {
            "${userId}_${interlocutor.id}"
        } else {
            "${interlocutor.id}_${userId}"
        }

        return Conversation(
            id = conversationId,
            interlocutor = interlocutor,
            createdAt = LocalDateTime.now(ZoneOffset.UTC),
            state = ConversationState.DRAFT
        )
    }

    suspend fun createLocally(conversation: Conversation) {
        conversationRepository.upsertLocalConversation(conversation.copy(state = ConversationState.CREATING))
    }

    fun createRemoteConversation(conversation: Conversation, userId: String, senderId: String) {
        when (conversation.state) {
            ConversationState.DRAFT, ConversationState.ERROR ->
                conversationRepository.createRemoteConversation(conversation, senderId)

            ConversationState.SOFT_DELETED ->
                conversationRepository.unDeleteRemoteConversation(conversation, userId)

            ConversationState.CREATING -> {
                val duration = Duration.between(conversation.createdAt, LocalDateTime.now(ZoneOffset.UTC))
                if (duration.seconds > 10) {
                    conversationRepository.createRemoteConversation(conversation, senderId)
                }
            }

            else -> Unit
        }
    }
}