package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.withTimeout
import java.time.LocalDateTime

class DeleteConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(conversation: Conversation, userId: String) {
        withTimeout(15000) {
            conversationRepository.getRemoteConversationState(conversation.id, conversation.interlocutor.id)?.let { state ->
                if (state == ConversationState.SOFT_DELETED) {
                    conversationRepository.hardDeleteConversation(conversation.id)
                    messageRepository.deleteRemoteMessages(conversation.id)
                } else {
                    conversationRepository.softDeleteConversation(
                        conversation.copy(deleteTime = LocalDateTime.now()),
                        userId
                    )
                }
            }
        }
        messageRepository.deleteLocalMessages(conversation.id)
    }
}