package com.upsaclay.message.domain.usecase

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.LocalDateTimeAdapter
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usecase.GenerateRandomIdUseCase
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.toFcm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SendMessageUseCase(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val notificationUseCase: NotificationUseCase,
    private val scope: CoroutineScope
) {
    operator fun invoke(conversation: Conversation, user: User, content: String) {
        val message = newMessage(conversation, content, user.id)
        scope.launch {
            if (conversation.shouldBeCreated) {
                createConversation(conversation, user.id)
            }
            createMessage(message)
            sendNotification(conversation, message, user)
        }
    }

    private suspend fun createConversation(conversation: Conversation, userId: String) {
        try {
            conversationRepository.upsertLocalConversation(conversation.copy(state = ConversationState.LOADING))
            conversationRepository.createConversation(conversation.copy(state = ConversationState.CREATED), userId)
        } catch (_: Exception) {
            conversationRepository.upsertLocalConversation(conversation.copy(state = ConversationState.ERROR))
        }
    }

    private suspend fun createMessage(message: Message) {
        try {
            messageRepository.upsertLocalMessage(message)
            messageRepository.createMessage(message.copy(state = MessageState.SENT))
        } catch (e: Exception) {
            messageRepository.upsertLocalMessage(message.copy(state = MessageState.ERROR))
        }
    }

    private fun newMessage(conversation: Conversation, content: String, userId: String): Message {
        return Message(
            id = GenerateRandomIdUseCase.intId,
            conversationId = conversation.id,
            senderId = userId,
            recipientId = conversation.interlocutor.id,
            content = content,
            date = LocalDateTime.now(),
            state = MessageState.LOADING
        )
    }

    private suspend fun sendNotification(conversation: Conversation, message: Message, user: User) {
        val fcmMessage = ConversationMessage(conversation, message).toFcm(user)
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
            .create()
        notificationUseCase.sendNotification(fcmMessage, gson)
    }
}