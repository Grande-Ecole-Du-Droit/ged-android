package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.repository.WhiteListRepository
import com.upsaclay.common.domain.userFixture
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val whiteListRepository: WhiteListRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: RegisterUseCase
    private val email = userFixture.email
    private val password = "password123"
    private val firstName = userFixture.firstName
    private val lastName = userFixture.lastName
    private val schoolLevel = userFixture.schoolLevel

    @Before
    fun setUp() {
        every { connectivityObserver.isConnected } returns true
        coEvery { whiteListRepository.isUserWhiteListed(any()) } returns true
        coEvery {
            authenticationRepository.registerWithEmailAndPassword(
                any(),
                any()
            )
        } returns userFixture.id
        coEvery { userRepository.createUser(any()) } returns Unit
        coEvery { authenticationRepository.setAuthenticated(any()) } returns Unit

        useCase = RegisterUseCase(
            authenticationRepository = authenticationRepository,
            userRepository = userRepository,
            whiteListRepository = whiteListRepository,
            connectivityObserver = connectivityObserver
        )
    }

    @Test(expected = NoInternetConnectionException::class)
    fun register_should_throw_NoInternetConnectionException_when_not_connected() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false

        // When
        useCase(email, password, firstName, lastName, schoolLevel)
    }

    @Test
    fun register_should_create_user_when_registered() = runTest {
        // When
        useCase(email, password, firstName, lastName, schoolLevel)

        // Then
        coEvery { userRepository.createUser(any()) } returns Unit
        coEvery { authenticationRepository.setAuthenticated(true) } returns Unit
    }
}