package com.upsaclay.gedoise.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.domain.usecase.ListenRemoteUserUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteUserUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var useCase: ListenRemoteUserUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setup() {
        every { userRepository.user } returns flowOf(userFixture)
        every { userRepository.getUserFlow(any()) } returns flowOf(userFixture)

        useCase = ListenRemoteUserUseCase(
            authenticationRepository = authenticationRepository,
            userRepository = userRepository,
            scope = testScope
        )
    }

    @Test
    fun start_should_synchronize_local_user_with_remote() {
        // When
        useCase.start()

        // Then
        coVerify { userRepository.storeUser(userFixture) }
    }

    @Test
    fun start_should_logout_when_remote_user_is_null() {
        // When
        useCase.start()

        // Then
        coVerify { authenticationRepository.logout() }
    }

    @Test
    fun stop_should_stop_job_listening() {
        // When
        useCase.start()
        useCase.stop()

        // Then
        assert(useCase.job!!.isCancelled)
    }
}