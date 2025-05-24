package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()

    private lateinit var useCase: CreateConversationUseCase

    @Before
    fun setUp() {
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } returns Unit
        coEvery { conversationRepository.unDeleteRemoteConversation(any(), any()) } returns Unit

        useCase = CreateConversationUseCase(conversationRepository)
    }

    @Test
    fun generateNewConversation_should_return_conversation_with_correct_id() {
        // Given
        val userId = "user1"
        val interlocutorId = "user2"

        // When
        val conversation = useCase.generateNewConversation(userId, userFixture2.copy(id = interlocutorId))

        // Then
        assert(conversation.id == "user1_user2" || conversation.id == "user2_user1")
    }

    @Test
    fun createLocally_should_upsert_local_conversation() = runTest {
        // When
        useCase.createLocally(conversationFixture)

        // Then
        coEvery {
            conversationRepository.upsertLocalConversation(
                conversationFixture.copy(state = ConversationState.CREATING)
            )
        }
    }

    @Test
    fun createRemotely_should_create_uncreated_conversation() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase.createRemoteConversation(conversation, userFixture.id, userFixture2.id)

        // Then
        coEvery {
            conversationRepository.createRemoteConversation(conversation, userFixture2.id)
        }
    }

    @Test
    fun createRemoteConversation_should_undelete_soft_deleted_conversation() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.SOFT_DELETED)

        // When
        useCase.createRemoteConversation(conversation, userFixture.id, userFixture2.id)

        // Then
        coEvery {
            conversationRepository.unDeleteRemoteConversation(conversation, userFixture.id)
        }
    }

    @Test
    fun createRemotely_should_create_conversation_when_creating_state_more_than_10_seconds() = runTest {
        // Given
        val conversation = conversationFixture.copy(
            createdAt = LocalDateTime.now(ZoneOffset.UTC).minusSeconds(11),
            state = ConversationState.CREATING
        )

        // When
        useCase.createRemoteConversation(conversation, userFixture.id, userFixture2.id)

        // Then
        coEvery {
            conversationRepository.createRemoteConversation(conversation, userFixture2.id)
        }
    }

    @Test(expected = Exception::class)
    fun createRemoteConversation_should_upsert_local_conversation_with_error_state() = runTest {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)
        coEvery {
            conversationRepository.createRemoteConversation(conversation, userFixture2.id)
        }.throws(Exception())

        // When
        useCase.createRemoteConversation(conversation, userFixture.id, userFixture2.id)

        // Then
        coEvery {
            conversationRepository.upsertLocalConversation(
                conversation.copy(state = ConversationState.ERROR)
            )
        }
    }
}