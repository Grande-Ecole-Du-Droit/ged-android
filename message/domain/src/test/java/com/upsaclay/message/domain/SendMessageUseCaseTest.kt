package com.upsaclay.message.domain

import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SendMessageUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val createConversationUseCase: CreateConversationUseCase = mockk()
    private val notificationUseCase: NotificationUseCase = mockk()

    private lateinit var useCase: SendMessageUseCase

    @Before
    fun setUp() {
        coEvery { conversationRepository.unDeleteRemoteConversation(any(), any()) } returns Unit
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { messageRepository.createLocalMessage(any()) } returns Unit
        coEvery { messageRepository.createRemoteMessage(any()) } returns Unit
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit
        coEvery { notificationUseCase.sendNotification<Any>(any(), any()) } returns Unit

        useCase = SendMessageUseCase(
            messageRepository = messageRepository,
            createConversationUseCase = createConversationUseCase,
            notificationUseCase = notificationUseCase
        )
    }

    @Test
    fun sendMessageUseCase_should_create_local_message() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { messageRepository.createLocalMessage(any()) }
    }

    @Test
    fun sendMessageUseCase_should_create_remote_message() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { messageRepository.createRemoteMessage(any()) }
    }

    @Test
    fun sendMessageUseCase_should_send_notification() = runTest {
        // When
        useCase(conversationFixture, userFixture, "content")

        // Then
        coEvery { notificationUseCase.sendNotification<Any>(any(), any()) }
    }

    @Test
    fun sendMessageUseCase_should_create_conversation_when_state_is_not_created() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase(conversation, userFixture, "content")

        // Then
        coEvery { conversationRepository.createRemoteConversation(conversation, userFixture.id) }
    }

    @Test
    fun sendMessageUseCase_should_enable_conversation_when_state_is_deleted() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.SOFT_DELETED)

        // When
        useCase(conversation, userFixture, "content")

        // Then
        coEvery { conversationRepository.unDeleteRemoteConversation(conversation, userFixture.id) }
    }

    @Test
    fun sendMessageUseCase_should_set_conversation_to_error_when_exception_is_thrown() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)
        coEvery { messageRepository.createRemoteMessage(any()) } throws Exception("Network error")

        // When
        useCase(conversation, userFixture, "content")

        // Then
        coEvery {
            conversationRepository.upsertLocalConversation(
                conversation.copy(state = ConversationState.ERROR)
            )
        }
    }

    @Test
    fun sendMessageUseCase_should_not_update_conversation_state_to_error_when_state_is_creating() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.CREATING)
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } throws Exception("Network error")

        // When
        useCase(conversation, userFixture, "content")

        // Then
        coEvery {
            conversationRepository.upsertLocalConversation(
                conversation.copy(state = ConversationState.CREATING)
            )
        }
    }
}