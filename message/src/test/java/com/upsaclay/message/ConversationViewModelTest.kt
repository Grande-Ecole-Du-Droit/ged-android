package com.upsaclay.message

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.conversationsMessageFixture
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.presentation.conversation.ConversationViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConversationViewModelTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val conversationMessageRepository: ConversationMessageRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val deleteConversationUseCase: DeleteConversationUseCase = mockk()

    private lateinit var conversationViewModel: ConversationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.currentUser } returns userFixture
        coEvery { conversationRepository.getLocalConversation(any()) } returns conversationFixture
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } returns Unit
        every { conversationMessageRepository.conversationsMessage } returns flowOf(conversationsMessageFixture)
        coEvery { deleteConversationUseCase(any(), any()) } returns Unit

        conversationViewModel = ConversationViewModel(
            userRepository = userRepository,
            conversationMessageRepository = conversationMessageRepository,
            deleteConversationUseCase = deleteConversationUseCase
        )
    }

    @Test
    fun deleteConversation_delete_conversation() = runTest {
        // When
        conversationViewModel.deleteConversation(conversationFixture)

        // Then
        coVerify { deleteConversationUseCase(conversationFixture, userFixture.id) }
    }
}