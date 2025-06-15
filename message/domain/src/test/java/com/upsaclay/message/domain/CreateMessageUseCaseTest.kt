package com.upsaclay.message.domain

import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.CreateMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateMessageUseCaseTest {
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: CreateMessageUseCase

    @Before
    fun setUp() {
        coEvery { messageRepository.updateLocalMessage(any()) } returns Unit
        coEvery { messageRepository.createMessage(any()) } returns Unit

        useCase = CreateMessageUseCase(messageRepository)
    }

    @Test
    fun createMessage_should_create_message_with_loading_state() = runTest {
        // Given
        val message = messageFixture.copy(state = MessageState.DRAFT)

        // When
        useCase(message)

        // Then
        coVerify { messageRepository.createMessage(message.copy(state = MessageState.LOADING)) }
    }

    @Test
    fun createMessage_should_update_local_message_to_created_state_when_succeeds() = runTest {
        // Given
        val message = messageFixture.copy(state = MessageState.DRAFT)

        // When
        useCase(message)

        // Then
        coVerify { messageRepository.createMessage(message.copy(state = MessageState.LOADING)) }
    }

    @Test
    fun createMessage_should_update_local_message_state_to_error_state_when_fails() = runTest {
        // Given
        val message = messageFixture.copy(state = MessageState.DRAFT)
        coEvery { messageRepository.createMessage(any()) } throws Exception()

        // When
        useCase(message)

        // Then
        coVerify { messageRepository.updateLocalMessage(message.copy(state = MessageState.ERROR)) }
    }

}