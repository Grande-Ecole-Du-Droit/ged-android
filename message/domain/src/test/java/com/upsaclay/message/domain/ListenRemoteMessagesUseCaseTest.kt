package com.upsaclay.message.domain

import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse

class ListenRemoteMessagesUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()

    private lateinit var useCase: ListenRemoteMessagesUseCase

    private val testScope = TestScope(StandardTestDispatcher())

    @Before
    fun setUp() {
        every { conversationRepository.getConversationsFlow() } returns flowOf(listOf(conversationFixture))
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.fetchRemoteConversations(any(), any()) } returns flowOf(conversationFixture)
        coEvery { messageRepository.getLastMessage(any()) } returns messageFixture
        coEvery { messageRepository.fetchRemoteMessages(any(), any(), any()) } returns flowOf(messageFixture)
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit

        useCase = ListenRemoteMessagesUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            scope = testScope
        )
    }

    @Test
    fun getConversationsFlow_should_not_return_already_fetched_conversation() = runTest {
        // Given
        val conversations = listOf(
            conversationFixture,
            conversationFixture.copy(id = "another_new_conversation_id")
        )
        useCase.fetchedConversations = mutableMapOf(conversationFixture.id to conversationFixture)
        every { conversationRepository.getConversationsFlow() } returns flowOf(conversations)

        // When
        val result = useCase.getConversationsFlow()

        // Then
        assert(result.first().count() == 1)
    }

    @Test
    fun getConversationsFlow_should_update_fetched_conversations() = runTest {
        // Given
        val conversations = listOf(
            conversationFixture,
            conversationFixture.copy(id = "another_new_conversation_id")
        )
        every { conversationRepository.getConversationsFlow() } returns flowOf(conversations)

        // When
        useCase.getConversationsFlow().first()

        // Then
        assert(useCase.fetchedConversations.containsValue(conversations[0]))
        assert(useCase.fetchedConversations.containsValue(conversations[1]))
    }

    @Test
    fun listenRemoteMessages_should_listen_message_with_last_message_date_offset() = runTest {
        // When
        useCase.listenRemoteMessages(conversationsFixture)

        // Then
        coVerify {
            messageRepository.fetchRemoteMessages(
                conversationFixture.id,
                conversationFixture.interlocutor.id,
                messageFixture.date
            )
        }
    }

    @Test
    fun listenRemoteMessages_should_upsert_local_message() = runTest {
        // When
        useCase.listenRemoteMessages(conversationsFixture)

        // Then
        coVerify { messageRepository.upsertLocalMessage(messageFixture) }
    }

    @Test
    fun stop_should_stop_listening() = runTest {
        // Given
        useCase.start()

        // When
        useCase.stop()

        // Then
        assertFalse(useCase.job!!.isActive)
    }
}