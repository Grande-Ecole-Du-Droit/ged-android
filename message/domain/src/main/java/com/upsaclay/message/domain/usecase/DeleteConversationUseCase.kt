package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.withTimeout
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

class DeleteConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(conversation: Conversation, userId: String) {
        if (conversation.state == ConversationState.SOFT_DELETED) {
            conversationRepository.hardDeleteConversation(conversation)
            messageRepository.deleteRemoteMessages(conversation.id)
        } else {
            conversationRepository.softDeleteConversation(
                conversation.copy(deleteTime = LocalDateTime.now(ZoneOffset.UTC)),
                userId
            )
        }
        messageRepository.deleteLocalMessages(conversation.id)
    }
}