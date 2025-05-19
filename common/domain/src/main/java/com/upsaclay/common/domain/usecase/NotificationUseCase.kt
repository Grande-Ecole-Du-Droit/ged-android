package com.upsaclay.common.domain.usecase

import com.google.gson.Gson
import com.upsaclay.common.domain.FCMNotificationSender
import com.upsaclay.common.domain.entity.FCMMessage
import com.upsaclay.common.domain.entity.SystemEvent

class NotificationUseCase(
    private val fcmNotificationSender: FCMNotificationSender,
    private val sharedEventsUseCase: SharedEventsUseCase
) {
    suspend fun <T>sendNotification(fcmMessage: FCMMessage<T>, gson: Gson = Gson()) {
        fcmNotificationSender.sendNotification(gson.toJson(fcmMessage))
    }

    suspend fun clearNotifications(notificationGroupId: String) {
        val event = SystemEvent.ClearNotifications(notificationGroupId)
        sharedEventsUseCase.sendSharedEvent(event)
    }
}