package com.upsaclay.message

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.usecase.GetConversationUseCase
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
    private val getConversationUseCase: GetConversationUseCase = mockk()

    private lateinit var createConversationViewModel: CreateConversationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { userRepository.currentUser } returns userFixture
        coEvery { getConversationUseCase(any()) } returns conversationFixture
        coEvery { userRepository.getUsers() } returns usersFixture

        createConversationViewModel = CreateConversationViewModel(
            userRepository = userRepository,
            getConversationUseCase = getConversationUseCase
        )
    }

    @Test
    fun getConversation_should_return_conversation() = runTest {
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
            getConversationUseCase = getConversationUseCase
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

    @Test
    fun resetQuery_should_reset_query_and_users() = runTest {
        // Given
        createConversationViewModel.onQueryChange("test")

        // When
        createConversationViewModel.resetQuery()

        // Then
        assertEquals("", createConversationViewModel.uiState.value.query)
    }

    @Test
    fun resetQuery_should_reset_users_to_default() = runTest {
        // Given
        val defaultUsers = usersFixture.filterNot { it.id == userFixture.id }
        createConversationViewModel.onQueryChange("test")

        // When
        createConversationViewModel.resetQuery()

        // Then
        assertEquals(defaultUsers, createConversationViewModel.uiState.value.users)
    }
}