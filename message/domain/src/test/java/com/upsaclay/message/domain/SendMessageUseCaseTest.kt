package com.upsaclay.message.domain

import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SendMessageUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val createConversationUseCase: CreateConversationUseCase = mockk()
    private val notificationUseCase: NotificationUseCase = mockk()

    private lateinit var useCase: SendMessageUseCase

    @Before
    fun setUp() {
        coEvery { createConversationUseCase.createLocalConversation(any()) } returns Unit
        coEvery { createConversationUseCase.createRemoteConversation(any(), any(), any()) } returns Unit
        coEvery { messageRepository.createLocalMessage(any()) } returns Unit
        coEvery { messageRepository.createRemoteMessage(any()) } returns Unit
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit
        coEvery { notificationUseCase.sendNotification<Any>(any(), any()) } returns Unit

        useCase = SendMessageUseCase(
            messageRepository = messageRepository,
            createConversationUseCase = createConversationUseCase,
            notificationUseCase = notificationUseCase
        )
    }

    @Test
    fun sendMessageUseCase_should_create_local_message() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { messageRepository.createLocalMessage(any()) }
    }

    @Test
    fun sendMessageUseCase_should_create_remote_message() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { messageRepository.createRemoteMessage(any()) }
    }

    @Test
    fun sendMessageUseCase_should_send_notification() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { notificationUseCase.sendNotification<Any>(any(), any()) }
    }

    @Test
    fun sendMessageUseCase_should_create_conversation_when_state_is_not_created() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase(conversation, userFixture, "content")

        // Then
        coEvery { createConversationUseCase.createRemoteConversation(conversation, userFixture.id, userFixture2.id) }
    }
}