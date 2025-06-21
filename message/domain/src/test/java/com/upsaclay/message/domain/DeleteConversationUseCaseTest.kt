package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: DeleteConversationUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { conversationRepository.updateLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.deleteConversation(any(), any(), any()) } returns Unit
        coEvery { messageRepository.deleteLocalMessages(any()) } returns Unit

        useCase = DeleteConversationUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            scope = testScope
        )
    }

    @Test
    fun deleteConversation_should_delete_conversation() = runTest(testScope.testScheduler) {
        // When
        useCase(
            conversationFixture,
            userFixture.id
        )
        testScope.advanceUntilIdle()

        // Then
        coVerify { conversationRepository.deleteConversation(any(), userFixture.id, any()) }
    }

    @Test
    fun deleteConversation_should_delete_local_conversation_messages() = runTest(testScope.testScheduler) {
        // When
        useCase(conversationFixture, userFixture.id)
        testScope.advanceUntilIdle()

        // Then
        coVerify { messageRepository.deleteLocalMessages(any()) }
    }
}