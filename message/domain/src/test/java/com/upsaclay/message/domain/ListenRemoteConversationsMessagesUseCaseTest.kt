package com.upsaclay.message.domain

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsMessagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteConversationsMessagesUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val messageRepository: MessageRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()

    private lateinit var useCase: ListenRemoteConversationsMessagesUseCase

    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { userRepository.user } returns flowOf(userFixture)
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.fetchRemoteConversations(any()) } returns flowOf(conversationFixture)

        useCase = ListenRemoteConversationsMessagesUseCase(
            userRepository = userRepository,
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            scope = testScope
        )
    }

    @Test
    fun start_should_start_remote_conversations_listening() = runTest {
        // When
        useCase.start()

        // Then
        assert(useCase.job != null)
        coVerify { conversationRepository.fetchRemoteConversations(userFixture.id) }
    }

    @Test
    fun start_should_upsert_new_conversations() = runTest {
        // Given
        coEvery { conversationRepository.fetchRemoteConversations(any()) } returns flowOf(
            conversationFixture, conversationFixture.copy(id = "newId")
        )

        // When
        useCase.start()

        // Then
        coVerify {
            conversationRepository.upsertLocalConversation(conversationFixture)
            conversationRepository.upsertLocalConversation(conversationFixture.copy(id = "newId"))
        }
    }

    @Test
    fun start_should_not_upsert_known_conversations() = runTest {
        // Given
        coEvery { conversationRepository.fetchRemoteConversations(any()) } returns flowOf(conversationFixture, conversationFixture)

        // When
        useCase.start()

        // Then
        coVerify(exactly = 1) { conversationRepository.upsertLocalConversation(conversationFixture) }
    }

    @Test
    fun start_should_listen_remote_conversation_messages() = runTest {
        // When
        useCase.start()

        // Then
        coVerify { messageRepository.fetchRemoteMessages(conversationFixture.id, null) }
    }

    @Test
    fun stop_should_stop_conversations_listening() = runTest {
        // Given
        useCase.start()

        // When
        useCase.stop()

        // Then
        assertFalse(useCase.job!!.isActive)
    }
}