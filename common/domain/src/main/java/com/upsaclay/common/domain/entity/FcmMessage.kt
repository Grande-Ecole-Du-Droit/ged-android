package com.upsaclay.common.domain.entity

import com.google.gson.annotations.SerializedName

data class FcmMessage<T>(
    val recipientId: String,
    val notification: FcmNotification,
    val data: FcmData<T>,
    val priority: String = "high"
) {
    val icon: String = "ic_notification"
}

data class FcmNotification(
    val title: String,
    val body: String
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