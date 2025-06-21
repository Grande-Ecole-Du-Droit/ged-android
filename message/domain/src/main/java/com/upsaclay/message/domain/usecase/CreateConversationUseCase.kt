package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository

class CreateConversationUseCase(private val conversationRepository: ConversationRepository) {
    suspend operator fun invoke(conversation: Conversation, userId: String) {
        try {
            conversationRepository.createConversation(conversation.copy(state = ConversationState.CREATING), userId)
            conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.CREATED))
        } catch (_: Exception) {
            conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.ERROR))
        }
    }
}