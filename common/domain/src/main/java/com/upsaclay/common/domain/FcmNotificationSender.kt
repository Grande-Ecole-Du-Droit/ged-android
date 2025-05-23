package com.upsaclay.common.domain

interface FcmNotificationSender {
    suspend fun sendNotification(fcmMessage: String)
}