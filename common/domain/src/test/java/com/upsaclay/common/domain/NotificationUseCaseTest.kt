package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.SystemEvent
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.common.domain.usecase.SharedEventsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NotificationUseCaseTest {
    private val fcmNotificationSender: FCMNotificationSender = mockk()
    private val sharedEventsUseCase: SharedEventsUseCase = mockk()

    private lateinit var notificationUseCase: NotificationUseCase

    @Before
    fun setUp() {
        coEvery { fcmNotificationSender.sendNotification(any()) } returns Unit
        coEvery { sharedEventsUseCase.sendSharedEvent(any()) } returns Unit

        notificationUseCase = NotificationUseCase(
            fcmNotificationSender = fcmNotificationSender,
            sharedEventsUseCase = sharedEventsUseCase
        )
    }

    @Test
    fun sendNotificationToFCM_should_send_notification() = runTest {
        // When
        notificationUseCase.sendNotification(fcmFixture)

        // Then
        coVerify { fcmNotificationSender.sendNotification(any()) }
    }

    @Test
    fun clearNotifications_should_send_clear_notification_shared_event() = runTest {
        // Given
        val notificationGroupId = "notificationGroupId"

        // When
        notificationUseCase.clearNotifications(notificationGroupId)

        // Then
        coVerify { sharedEventsUseCase.sendSharedEvent(SystemEvent.ClearNotifications(notificationGroupId)) }
    }
}