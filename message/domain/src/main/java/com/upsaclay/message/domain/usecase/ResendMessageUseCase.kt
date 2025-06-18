package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.MessageRepository

class ResendMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(message: Message) {
        try {
            messageRepository.updateLocalMessage(message.copy(state = MessageState.LOADING))
            messageRepository.createRemoteMessage(message)
            messageRepository.updateLocalMessage(message.copy(state = MessageState.SENT))
        } catch (_: Exception) {
            messageRepository.updateLocalMessage(message.copy(state = MessageState.ERROR))
        }
    }
}