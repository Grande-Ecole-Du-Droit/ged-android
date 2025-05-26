package com.upsaclay.gedoise

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.DataListeningUseCase
import com.upsaclay.gedoise.presentation.viewmodels.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val dataListeningUseCase: DataListeningUseCase = mockk()
    private val clearDataUseCase: ClearDataUseCase = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()

    private lateinit var mainViewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { authenticationRepository.isAuthenticated } returns flowOf(true)
        coEvery { userRepository.getCurrentUser() } returns userFixture
        coEvery { userRepository.getUsers() } returns usersFixture
        coEvery { userRepository.getUser(any()) } returns userFixture
        coEvery { userRepository.storeUser(any()) } returns Unit
        coEvery { userRepository.deleteCurrentUser() } returns Unit
        coEvery { dataListeningUseCase.start() } returns Unit
        coEvery { dataListeningUseCase.stop() } returns Unit
        coEvery { clearDataUseCase() } returns Unit
        coEvery { authenticationRepository.logout() } returns Unit

        mainViewModel = MainViewModel(
            authenticationRepository = authenticationRepository,
            dataListeningUseCase = dataListeningUseCase,
            clearDataUseCase = clearDataUseCase
        )
    }

    @Test
    fun data_should_be_listening_when_user_is_authenticated() {
        // When
        mainViewModel.startListening()

        // Then
        coVerify { dataListeningUseCase.start() }
    }

    @Test
    fun data_should_be_not_listening_when_user_is_not_authenticated() {
        // Given
        every { authenticationRepository.isAuthenticated } returns flowOf(false)

        // When
        mainViewModel.startListening()

        // Then
        coVerify { dataListeningUseCase.stop() }
    }

    @Test
    fun data_should_be_cleared_when_user_is_not_authenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns flowOf(false)

        // When
        mainViewModel.startListening()

        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase() }
    }

    @Test
    fun data_should_be_deleted_when_user_is_not_authenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns flowOf(false)

        // When
        mainViewModel.startListening()

        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase() }
    }
}