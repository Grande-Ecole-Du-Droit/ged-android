package com.upsaclay.message

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.GetLocalConversationUseCase
import com.upsaclay.message.presentation.conversation.create.CreateConversationViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CreateConversationViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val getLocalConversationUseCase: GetLocalConversationUseCase = mockk()

    private lateinit var createConversationViewModel: CreateConversationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getLocalConversationUseCase(any()) } returns conversationFixture
        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { userRepository.currentUser } returns userFixture
        coEvery { userRepository.getUsers() } returns usersFixture

        createConversationViewModel = CreateConversationViewModel(
            userRepository = userRepository,
            getLocalConversationUseCase = getLocalConversationUseCase
        )
    }

    @Test
    fun getConversation_should_return_conversation_when_present() = runTest {
        // When
        val result = createConversationViewModel.getConversation(userFixture)

        // Then
        assertEquals(conversationFixture, result)
    }

    @Test
    fun all_users_should_be_fetched_except_current() = runTest {
        // Given
        val users = usersFixture.filterNot { it.id == userFixture.id }
        coEvery { userRepository.getUsers() } returns users

        // When
        createConversationViewModel = CreateConversationViewModel(
            userRepository = userRepository,
            getLocalConversationUseCase = getLocalConversationUseCase
        )

        // Then
        assertEquals(users, createConversationViewModel.uiState.value.users)
    }

    @Test
    fun onQueryChange_should_update_query() = runTest {
        // Given
        val query = "test"

        // When
        createConversationViewModel.onQueryChange(query)

        // Then
        assertEquals(query, createConversationViewModel.uiState.value.query)
    }

    @Test
    fun onQueryChange_should_filter_users_based_on_query() = runTest {
        // Given
        val query = "test"
        val filteredUsers = usersFixture.filter { it.fullName.contains(query, ignoreCase = true) }

        // When
        createConversationViewModel.onQueryChange(query)

        // Then
        assertEquals(filteredUsers, createConversationViewModel.uiState.value.users)
    }
}