package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SendMessageUseCase(
    private val createConversationUseCase: CreateConversationUseCase,
    private val createMessageUseCase: CreateMessageUseCase,
    private val messageNotificationUseCase: MessageNotificationUseCase,
    private val scope: CoroutineScope
) {
    operator fun invoke(message: Message, conversation: Conversation, userId: String) {
        scope.launch {
            if (conversation.shouldBeCreated) {
                createConversationUseCase(conversation, userId)
            }
            createMessageUseCase(message)
            messageNotificationUseCase.sendNotification(ConversationMessage(conversation, message))
        }
    }
}