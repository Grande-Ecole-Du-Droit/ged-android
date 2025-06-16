package com.upsaclay.gedoise.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.gedoise.domain.usecase.ListenRemoteUserUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteUserUseCaseTest {
    private val userRepository: UserRepository = mockk()

    private lateinit var useCase: ListenRemoteUserUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setup() {
        every { userRepository.user } returns flowOf(userFixture)
        every { userRepository.getUserFlow(any()) } returns flowOf(userFixture2)

        useCase = ListenRemoteUserUseCase(
            userRepository = userRepository,
            scope = testScope
        )
    }

    @Test
    fun start_should_update_local_user_when_different_from_remote() {
        // When
        useCase.start()
        testScope.advanceUntilIdle()

        // Then
        coVerify { userRepository.storeUser(userFixture2) }
    }

    @Test
    fun stop_should_stop_job_listening() {
        // When
        useCase.start()
        useCase.stop()

        // Then
        assertFalse(useCase.job!!.isActive)
    }
}