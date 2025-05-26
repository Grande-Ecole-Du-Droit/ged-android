package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(conversation: Conversation, userId: String) {
        scope.launch {
            conversationRepository.upsertLocalConversation(conversation.copy(state = ConversationState.LOADING))
            conversationRepository.deleteConversation(conversation, userId)
            messageRepository.deleteLocalMessages(conversation.id)
        }
    }
}