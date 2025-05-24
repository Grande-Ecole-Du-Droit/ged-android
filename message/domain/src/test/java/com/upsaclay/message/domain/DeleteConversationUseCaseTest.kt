package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: DeleteConversationUseCase

    @Before
    fun setUp() {
        coEvery { conversationRepository.hardDeleteConversation(any()) } returns Unit
        coEvery { conversationRepository.softDeleteConversation(any(), any()) } returns Unit
        coEvery { messageRepository.deleteLocalMessages(any()) } returns Unit
        coEvery { messageRepository.deleteRemoteMessages(any()) } returns Unit

        useCase = DeleteConversationUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository
        )
    }

    @Test
    fun deleteConversation_should_hard_delete_remote_conversation_and_message_when_state_is_deleted() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.SOFT_DELETED)

        // When
        useCase(conversation, userFixture.id)

        // Then
        coVerify { conversationRepository.hardDeleteConversation(conversation) }
        coVerify { messageRepository.deleteRemoteMessages(conversation.id) }
    }

    @Test
    fun deleteConversation_should_soft_delete_remote_conversation_when_state_is_not_deleted() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.CREATED)

        // When
        useCase(conversation, userFixture.id)

        // Then
        coVerify { conversationRepository.softDeleteConversation(any(), userFixture.id) }
    }

    @Test
    fun deleteConversation_should_delete_local_messages() = runTest {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coVerify { messageRepository.deleteLocalMessages(conversationFixture.id) }
    }
}