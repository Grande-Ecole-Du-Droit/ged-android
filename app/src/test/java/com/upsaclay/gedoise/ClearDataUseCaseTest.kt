package com.upsaclay.gedoise

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearDataUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: ClearDataUseCase

    @Before
    fun setUp() {
        coEvery { userRepository.deleteCurrentUser() } returns Unit
        coEvery { conversationRepository.deleteLocalConversations() } returns Unit
        coEvery { messageRepository.deleteLocalMessages() } returns Unit

        useCase = ClearDataUseCase(
            userRepository = userRepository,
            conversationRepository = conversationRepository,
            messageRepository = messageRepository
        )
    }

    @Test
    fun clearDataUseCase_should_delete_all_data() = runTest {
        // When
        useCase()

        // Then
        coVerify { userRepository.deleteCurrentUser() }
        coVerify { conversationRepository.deleteLocalConversations() }
        coVerify { messageRepository.deleteLocalMessages() }
    }
}