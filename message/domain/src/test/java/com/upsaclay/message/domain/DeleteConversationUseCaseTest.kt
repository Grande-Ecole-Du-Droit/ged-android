package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import io.mockk.coEvery
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
        coEvery { conversationRepository.getRemoteConversationState(any(), any()) } returns ConversationState.CREATED
        coEvery { conversationRepository.softDeleteConversation(any(), any()) } returns Unit
        coEvery { messageRepository.deleteLocalMessages(any()) } returns Unit

        useCase = DeleteConversationUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository
        )
    }

    @Test
    fun deleteConversation_should_hard_delete_remote_conversation_and_message_when_state_is_deleted() = runTest {
        // When
        useCase(conversationFixture.copy(state = ConversationState.DELETED), userFixture.id)

        // Then
        coEvery { conversationRepository.hardDeleteConversation(conversationFixture.id) }
        coEvery { messageRepository.deleteRemoteMessages(conversationFixture.id) }
    }

    @Test
    fun deleteConversation_should_soft_delete_remote_conversation_when_state_is_not_deleted() = runTest {
        // When
        useCase(conversationFixture.copy(state = ConversationState.CREATED), userFixture.id)

        // Then
        coEvery { conversationRepository.softDeleteConversation(conversationFixture, userFixture.id) }
    }

    @Test
    fun deleteConversation_should_delete_local_messages() = runTest {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coEvery { messageRepository.deleteLocalMessages(conversationFixture.id) }
    }
}