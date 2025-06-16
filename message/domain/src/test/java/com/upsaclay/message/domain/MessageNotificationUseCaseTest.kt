package com.upsaclay.message.domain

import com.upsaclay.common.domain.NotificationApi
import com.upsaclay.common.domain.entity.SystemEvent
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.SharedEventsUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.usecase.MessageNotificationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MessageNotificationUseCaseTest {
    private val notificationApi: NotificationApi = mockk()
    private val userRepository: UserRepository = mockk()
    private val sharedEventsUseCase: SharedEventsUseCase = mockk()

    private lateinit var useCase: MessageNotificationUseCase

    @Before
    fun setUp() {
        every { userRepository.currentUser } returns userFixture
        coEvery { notificationApi.sendNotification<Any>(any(), any(), any()) } returns Unit
        coEvery { sharedEventsUseCase.sendSharedEvent(any<SystemEvent.ClearNotifications>()) } returns Unit

        useCase = MessageNotificationUseCase(
            notificationApi = notificationApi,
            userRepository = userRepository,
            sharedEventsUseCase = sharedEventsUseCase
        )
    }

    @Test
    fun sendNotification_should_send_notification() = runTest {
        // When
        useCase.sendNotification(conversationMessageFixture)

        // Then
        coVerify { notificationApi.sendNotification<Any>(any(), any(), any()) }
    }

    @Test
    fun clearNotifications_should_send_clear_notification() = runTest {
        // Given
        val notificationGroupId = "notificationGroupId"

        // When
        useCase.clearNotifications(notificationGroupId)

        // Then
        coVerify { sharedEventsUseCase.sendSharedEvent(SystemEvent.ClearNotifications(notificationGroupId)) }
    }
}