package com.upsaclay.gedoise

import com.upsaclay.gedoise.domain.usecase.DataListeningUseCase
import com.upsaclay.gedoise.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsMessagesUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DataListeningUseCaseTest {
    private val listenRemoteConversationsMessagesUseCase: ListenRemoteConversationsMessagesUseCase = mockk()
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase = mockk()

    private lateinit var useCase: DataListeningUseCase

    @Before
    fun setUp() {
        every { listenRemoteConversationsMessagesUseCase.start() } returns Unit
        every { listenRemoteConversationsMessagesUseCase.stop() } returns Unit
        every { listenRemoteUserUseCase.start() } returns Unit
        every { listenRemoteUserUseCase.stop() } returns Unit

        useCase = DataListeningUseCase(
            listenRemoteConversationsMessagesUseCase = listenRemoteConversationsMessagesUseCase,
            listenRemoteUserUseCase = listenRemoteUserUseCase
        )
    }

    @Test
    fun start_should_start_listening_data() = runTest {
        // When
        useCase.start()

        // Then
        coVerify { listenRemoteConversationsMessagesUseCase.start() }
        coVerify { listenRemoteUserUseCase.start() }
    }

    @Test
    fun stop_should_stop_listening_data() = runTest {
        // When
        useCase.stop()

        // Then
        coVerify { listenRemoteConversationsMessagesUseCase.stop() }
    }
}