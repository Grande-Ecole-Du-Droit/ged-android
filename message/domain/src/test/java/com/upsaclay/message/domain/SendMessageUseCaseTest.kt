package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import com.upsaclay.message.domain.usecase.CreateMessageUseCase
import com.upsaclay.message.domain.usecase.MessageNotificationUseCase
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
    private val createConversationUseCase: CreateConversationUseCase = mockk()
    private val createMessageUseCase: CreateMessageUseCase = mockk()
    private val messageNotificationUseCase: MessageNotificationUseCase = mockk()

    private lateinit var useCase: SendMessageUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { messageNotificationUseCase.sendNotification(any()) } returns Unit

        useCase = SendMessageUseCase(
            createConversationUseCase = createConversationUseCase,
            createMessageUseCase = createMessageUseCase,
            messageNotificationUseCase = messageNotificationUseCase,
            scope = testScope
        )
    }

    @Test
    fun sendMessageUseCase_should_create_conversation_when_not_created() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase(
            messageFixture,
            conversation,
            userFixture.id
        )

        // Then
        coVerify { createConversationUseCase(conversation, userFixture.id) }
    }

    @Test
    fun sendMessageUseCase_should_create_message() = runTest {
        // When
        useCase(messageFixture, conversationFixture, userFixture.id)

        // Then
        coEvery { createMessageUseCase(messageFixture) }
    }

    @Test
    fun sendMessageUseCase_should_send_notification() = runTest {
        // When
        useCase(messageFixture, conversationFixture, userFixture.id)

        // Then
        coEvery { messageNotificationUseCase.sendNotification(any()) }
    }
}