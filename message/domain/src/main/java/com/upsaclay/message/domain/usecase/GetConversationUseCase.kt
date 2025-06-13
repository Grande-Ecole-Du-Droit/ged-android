package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

class GetConversationUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(interlocutor: User): Conversation {
        return conversationRepository.getConversation(interlocutor.id) ?: run {
            val user = requireNotNull(userRepository.currentUser)
            generateNewConversation(user.id, interlocutor)
        }
    }

    private fun generateNewConversation(userId: String, interlocutor: User): Conversation {
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
}