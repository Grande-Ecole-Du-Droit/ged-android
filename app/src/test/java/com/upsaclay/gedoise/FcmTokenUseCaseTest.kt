package com.upsaclay.gedoise

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.fcmTokenFixture
import com.upsaclay.common.domain.repository.CredentialsRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.usecase.FcmTokenUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FcmTokenUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val credentialsRepository: CredentialsRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: FcmTokenUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { authenticationRepository.authenticated } returns flowOf(true)
        every { connectivityObserver.connected } returns flowOf(true)
        coEvery { credentialsRepository.getUnsentFcmToken() } returns fcmTokenFixture
        coEvery { credentialsRepository.removeUnsentFcmToken() } returns Unit
        coEvery { credentialsRepository.storeUnsentFcmToken(any()) } returns Unit
        coEvery { credentialsRepository.sendFcmToken(any()) } returns Unit

        useCase = FcmTokenUseCase(
            userRepository = userRepository,
            authenticationRepository = authenticationRepository,
            credentialsRepository = credentialsRepository,
            connectivityObserver = connectivityObserver,
            scope = testScope
        )
    }

    @Test
    fun fcmTokenUseCase_should_send_unsent_token_when_authenticated() {
        // When
        useCase.listenEvents()

        // Then
        coVerify { credentialsRepository.sendFcmToken(fcmTokenFixture) }
    }

    @Test
    fun fcmTokenUseCase_should_delete_token_when_unauthenticated() {
        // Given
        coEvery { authenticationRepository.authenticated } returns MutableStateFlow(false)

        // When
        useCase.listenEvents()

        // Then
        coVerify { credentialsRepository.removeUnsentFcmToken() }
    }

    @Test
    fun fcmTokenUseCase_sendFcmToken_should_remove_unsent_token_when_success() = runTest {
        // Given

        // When
        useCase.sendFcmToken(fcmTokenFixture)

        // Then
        coVerify { credentialsRepository.removeUnsentFcmToken() }
    }

    @Test
    fun fcmTokenUseCase_sendFcmToken_should_store_unsent_token_when_fails() = runTest {
        // Given
        coEvery { credentialsRepository.sendFcmToken(any()) } throws Exception()

        // When
        useCase.sendFcmToken(fcmTokenFixture)

        // Then
        coVerify { credentialsRepository.storeUnsentFcmToken(fcmTokenFixture) }
    }

    @Test
    fun fcmTokenUseCase_storeToken_should_store_token() = runTest {
        // When
        useCase.storeToken(fcmTokenFixture)

        // Then
        coVerify { credentialsRepository.storeUnsentFcmToken(fcmTokenFixture) }
    }
}