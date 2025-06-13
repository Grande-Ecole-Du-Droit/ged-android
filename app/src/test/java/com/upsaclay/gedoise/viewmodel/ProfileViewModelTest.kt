package com.upsaclay.gedoise.viewmodel

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.ProfileViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()

    private lateinit var profileViewModel: ProfileViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { authenticationRepository.logout() } returns Unit

        profileViewModel = ProfileViewModel(
            userRepository = userRepository,
            authenticationRepository = authenticationRepository
        )
    }

    @Test
    fun logout_should_logout_user() {
        // When
        profileViewModel.logout()

        // Then
        coVerify { authenticationRepository.logout() }
    }
}