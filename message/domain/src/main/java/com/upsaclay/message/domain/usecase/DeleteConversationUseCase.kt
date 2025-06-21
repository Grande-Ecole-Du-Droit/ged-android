package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class DeleteConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(conversation: Conversation, userId: String) {
        val deleteTime = LocalDateTime.now(ZoneOffset.UTC)
        val updatedConversation = conversation.copy(deleteTime = deleteTime)
        scope.launch {
            conversationRepository.updateLocalConversation(updatedConversation.copy(state = ConversationState.DELETING))
            conversationRepository.deleteConversation(updatedConversation, userId, deleteTime)
            messageRepository.deleteLocalMessages(conversation.id)
        }
    }
}