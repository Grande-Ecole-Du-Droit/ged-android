package com.upsaclay.common.domain.entity

import com.google.gson.annotations.SerializedName

data class FcmMessage<T>(
    val data: FcmData<T>,
    val android: FcmAndroid
)

data class FcmData<T>(
    val type: FcmDataType,
    val value: T
)

enum class FcmDataType {
    @SerializedName("message")
    MESSAGE;

    override fun toString(): String {
        return when (this) {
            MESSAGE -> "message"
        }
    }
}

data class FcmAndroid(
    val notification: FcmAndroidNotification,
    val priority: AndroidMessagePriority = AndroidMessagePriority.HIGH
)

data class FcmAndroidNotification(
    @SerializedName("channel_id")
    val channelId: String,
) {
    val icon: String = "ic_notification"
}

enum class AndroidMessagePriority {
    @SerializedName("high")
    HIGH;

    override fun toString(): String {
        return when (this) {
            HIGH -> "high"
        }
    }
}