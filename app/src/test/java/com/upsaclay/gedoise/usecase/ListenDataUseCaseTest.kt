package com.upsaclay.gedoise.usecase

import com.upsaclay.gedoise.domain.usecase.ListenDataUseCase
import com.upsaclay.gedoise.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ListenDataUseCaseTest {
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase = mockk()
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase = mockk()
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase = mockk()

    private lateinit var useCase: ListenDataUseCase

    @Before
    fun setUp() {
        every { listenRemoteMessagesUseCase.start() } returns Unit
        every { listenRemoteMessagesUseCase.stop() } returns Unit
        every { listenRemoteConversationsUseCase.start() } returns Unit
        every { listenRemoteConversationsUseCase.stop() } returns Unit
        every { listenRemoteUserUseCase.start() } returns Unit
        every { listenRemoteUserUseCase.stop() } returns Unit

        useCase = ListenDataUseCase(
            listenRemoteConversationsUseCase = listenRemoteConversationsUseCase,
            listenRemoteMessagesUseCase = listenRemoteMessagesUseCase,
            listenRemoteUserUseCase = listenRemoteUserUseCase
        )
    }

    @Test
    fun start_should_start_listening_remote_data() = runTest {
        // When
        useCase.start()

        // Then
        coVerify { listenRemoteConversationsUseCase.start() }
        coVerify { listenRemoteMessagesUseCase.start() }
        coVerify { listenRemoteUserUseCase.start() }
    }

    @Test
    fun stop_should_stop_listening_data() = runTest {
        // When
        useCase.stop()

        // Then
        coVerify { listenRemoteConversationsUseCase.stop() }
        coVerify { listenRemoteMessagesUseCase.stop() }
        coVerify { listenRemoteUserUseCase.stop() }
    }
}