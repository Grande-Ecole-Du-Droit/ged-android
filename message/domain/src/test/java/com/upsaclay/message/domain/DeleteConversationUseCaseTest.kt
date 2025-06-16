package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
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
class DeleteConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: DeleteConversationUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { conversationRepository.deleteConversation(any(), any()) } returns Unit
        coEvery { messageRepository.deleteLocalMessages(any()) } returns Unit

        useCase = DeleteConversationUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            scope = testScope
        )
    }

    @Test
    fun deleteConversation_should_delete_conversation() = runTest {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coVerify { conversationRepository.deleteConversation(conversationFixture, userFixture.id) }
    }

    @Test
    fun deleteConversation_should_delete_local_conversation_messages() = runTest {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coVerify { messageRepository.deleteLocalMessages(conversationFixture.id) }
    }
}