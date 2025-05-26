package com.upsaclay.message.domain

import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMessageUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()
    private val notificationUseCase: NotificationUseCase = mockk()

    private lateinit var useCase: SendMessageUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.createConversation(any(), any()) } returns Unit
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit
        coEvery { messageRepository.createMessage(any()) } returns Unit
        coEvery { notificationUseCase.sendNotification<Any>(any(), any()) } returns Unit

        useCase = SendMessageUseCase(
            messageRepository = messageRepository,
            conversationRepository = conversationRepository,
            notificationUseCase = notificationUseCase,
            scope = testScope
        )
    }

    @Test
    fun sendMessageUseCase_should_send_notification() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { notificationUseCase.sendNotification<Any>(any(), any()) }
    }

    @Test
    fun sendMessageUseCase_should_create_conversation_if_needed() = runTest {
        // When
        useCase(
            conversationFixture.copy(state = ConversationState.DRAFT),
            userFixture, "content"
        )

        // Then
        coVerify {
            conversationRepository.createConversation(
                conversationFixture.copy(state = ConversationState.CREATED), userFixture.id
            )
        }
    }

    @Test
    fun sendMessageUseCase_should_create_message() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { messageRepository.upsertLocalMessage(any()) }
    }
}