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
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.toFcm
import kotlinx.coroutines.withTimeout
import java.time.LocalDateTime

class SendMessageUseCase(
    private val messageRepository: MessageRepository,
    private val createConversationUseCase: CreateConversationUseCase,
    private val notificationUseCase: NotificationUseCase
) {
    suspend operator fun invoke(
        conversation: Conversation,
        user: User,
        content: String
    ) {
        val message = newMessage(conversation, content, user.id)
        try {
            createDataLocally(conversation, message)
            withTimeout(15000) {
                createDataRemotely(conversation, message, user.id)
            }
            sendNotification(conversation, message, user)
        } catch (_: Exception) {
            messageRepository.upsertLocalMessage(message.copy(state = MessageState.ERROR))
        }
    }

    private suspend fun createDataLocally(conversation: Conversation, message: Message) {
        if (conversation.state != ConversationState.CREATED) {
            createConversationUseCase.createLocally(conversation)
        }
        messageRepository.createLocalMessage(message)
    }

    private suspend fun createDataRemotely(conversation: Conversation, message: Message, userId: String) {
        if (conversation.state != ConversationState.CREATED) {
            createConversationUseCase.createRemotely(conversation, userId, message.senderId)
        }
        messageRepository.createRemoteMessage(message)
    }

    private fun newMessage(conversation: Conversation, content: String, userId: String): Message {
        val dateTime = LocalDateTime.now()
        return Message(
            id = GenerateRandomIdUseCase.intId,
            conversationId = conversation.id,
            senderId = userId,
            recipientId = conversation.interlocutor.id,
            content = content,
            date = dateTime,
            state = MessageState.SENDING
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