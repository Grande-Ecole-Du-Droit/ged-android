package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.entity.SystemEvent

abstract class NotificationUseCase<T>(
    private val sharedEventsUseCase: SharedEventsUseCase
) {
    abstract suspend fun sendNotification(data: T)

    suspend fun clearNotifications(notificationGroupId: String) {
        val event = SystemEvent.ClearNotifications(notificationGroupId)
        sharedEventsUseCase.sendSharedEvent(event)
    }
}