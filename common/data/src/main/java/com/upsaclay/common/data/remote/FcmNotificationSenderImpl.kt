package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.domain.FcmNotificationSender
import com.upsaclay.common.domain.e

internal class FcmNotificationSenderImpl(private val fcmApi: FcmApi): FcmNotificationSender {
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