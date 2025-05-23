package com.upsaclay.common.domain.usecase

import com.google.gson.Gson
import com.upsaclay.common.domain.FcmNotificationSender
import com.upsaclay.common.domain.entity.FcmMessage
import com.upsaclay.common.domain.entity.SystemEvent

class NotificationUseCase(
    private val fcmNotificationSender: FcmNotificationSender,
    private val sharedEventsUseCase: SharedEventsUseCase
) {
    suspend fun <T>sendNotification(fcmMessage: FcmMessage<T>, gson: Gson = Gson()) {
        fcmNotificationSender.sendNotification(gson.toJson(fcmMessage))
    }

    suspend fun clearNotifications(notificationGroupId: String) {
        val event = SystemEvent.ClearNotifications(notificationGroupId)
        sharedEventsUseCase.sendSharedEvent(event)
    }
}