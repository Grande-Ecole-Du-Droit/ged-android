package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()

    private lateinit var useCase: CreateConversationUseCase

    @Before
    fun setUp() {
        coEvery { conversationRepository.updateLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.createConversation(any(), any()) } returns Unit

        useCase = CreateConversationUseCase(conversationRepository)
    }

    @Test
    fun createConversation_should_create_conversation_with_loading_state() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase(conversation, userFixture.id)

        // Then
        coVerify {
            conversationRepository.createConversation(
                conversation.copy(state = ConversationState.LOADING),
                userFixture.id
            )
        }
    }

    @Test
    fun createConversation_should_update_local_conversation_to_created_state_when_succeeds() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase(conversation, userFixture.id)

        // Then
        coVerify {
            conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.CREATED)) }
    }

    @Test
    fun createConversation_should_update_local_conversation_state_to_error_state_when_fails() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)
        coEvery { conversationRepository.createConversation(any(), any()) } throws Exception()

        // When
        useCase(conversation, userFixture.id)

        // Then
        coVerify { conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.ERROR)) }
    }
}