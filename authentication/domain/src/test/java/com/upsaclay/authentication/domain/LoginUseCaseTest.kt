package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: LoginUseCase
    private val email = "email@example.com"
    private val password = "password123"

    @Before
    fun setUp() {
        every { connectivityObserver.isConnected } returns true
        coEvery { authenticationRepository.loginWithEmailAndPassword(any(), any()) } returns Unit
        coEvery { userRepository.getUserWithEmail(any()) } returns userFixture
        coEvery { userRepository.storeUser(any()) } returns Unit
        coEvery { authenticationRepository.setAuthenticated(any()) } returns Unit

        useCase = LoginUseCase(
            authenticationRepository = authenticationRepository,
            userRepository = userRepository,
            connectivityObserver = connectivityObserver
        )
    }

    @Test(expected = NoInternetConnectionException::class)
    fun login_should_throw_NoInternetConnectionException_when_not_connected() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false

        // When
        useCase(email, password)
    }

    @Test
    fun login_should_store_user_when_authentication_succeeds() = runTest {
        // When
        useCase(email, password)

        // Then
        coEvery { userRepository.getUserWithEmail(email) } returns userFixture
        coEvery { userRepository.storeUser(userFixture) } returns Unit
        coEvery { authenticationRepository.setAuthenticated(true) } returns Unit
    }

    @Test(expected = InvalidCredentialsException::class)
    fun login_should_throw_InvalidCredentialsException_when_user_not_found() = runTest {
        // Given
        coEvery { userRepository.getUserWithEmail(email) } returns null

        // When
        useCase(email, password)
    }

    @Test(expected = TimeoutCancellationException::class)
    fun login_should_throw_TimeoutCancellationException_when_takes_more_10_seconds() = runTest {
        // Given
        coEvery { authenticationRepository.loginWithEmailAndPassword(email, password) } just awaits

        // When
        useCase(email, password)
    }
}