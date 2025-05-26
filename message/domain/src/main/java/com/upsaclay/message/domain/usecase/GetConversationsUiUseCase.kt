package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.repository.ConversationMessageRepository
import com.upsaclay.message.domain.toConversationUI
import kotlinx.coroutines.flow.map

class GetConversationsUiUseCase(
    private val conversationMessageRepository: ConversationMessageRepository
) {
    operator fun invoke() = conversationMessageRepository.conversationsMessage
        .map { conversationMessages ->
            conversationMessages.map { it.toConversationUI() }
        }
}