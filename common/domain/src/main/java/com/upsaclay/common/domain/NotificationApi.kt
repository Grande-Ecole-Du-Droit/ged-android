package com.upsaclay.common.domain

import com.google.gson.Gson
import com.upsaclay.common.domain.entity.FcmMessage

interface NotificationApi {
    suspend fun <T> sendNotification(recipientId: String, fcmMessage: FcmMessage<T>, gson: Gson = Gson())
}