package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.FCMApi
import com.upsaclay.common.domain.FCMNotificationSender
import com.upsaclay.common.domain.e

internal class FCMNotificationSenderImpl(private val fcmApi: FCMApi): FCMNotificationSender {
    override suspend fun sendNotification(fcmMessage: String) {
        try {
            val response = fcmApi.sendNotification(fcmMessage)
            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string()
                    ?: "Failed to send FCM notification: ${response.errorBody()}"
                e(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Failed to send FCM notification: ${e.message}"
            e(errorMessage)
        }
    }
}