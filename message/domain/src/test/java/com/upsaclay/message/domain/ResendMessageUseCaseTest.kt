package com.upsaclay.message.domain

import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.ResendMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ResendMessageUseCaseTest {
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: ResendMessageUseCase

    @Before
    fun setUp() {
        coEvery { messageRepository.createRemoteMessage(any()) } returns Unit
        coEvery { messageRepository.updateLocalMessage(any()) } returns Unit

        useCase = ResendMessageUseCase(
            messageRepository = messageRepository
        )
    }

    @Test
    fun resendMessage_should_update_message_state_to_loading() = runTest {
        // When
        useCase(messageFixture)

        // Then
         coVerify { messageRepository.updateLocalMessage(messageFixture.copy(state = MessageState.SENDING)) }
    }

    @Test
    fun resendMessage_should_update_message_state_to_sent_when_succeed() = runTest {
        // When
        useCase(messageFixture)

        // Then
        coVerify { messageRepository.updateLocalMessage(messageFixture.copy(state = MessageState.SENT)) }
    }

    @Test
    fun resendMessage_should_update_message_state_to_error_when_fails() = runTest {
        // Given
        coEvery { messageRepository.createRemoteMessage(any()) } throws Exception()

        // When
        useCase(messageFixture)

        // Then
        coVerify { messageRepository.updateLocalMessage(messageFixture.copy(state = MessageState.ERROR)) }
    }
}