package com.upsaclay.message.data.remote.api

import com.google.firebase.Timestamp
import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.flow.Flow

internal interface MessageApi {
    fun listenMessages(conversationId: String, interlocutorId: String, offsetTime: Timestamp?): Flow<RemoteMessage>

    suspend fun createMessage(remoteMessage: RemoteMessage)

    suspend fun updateSeenMessage(remoteMessage: RemoteMessage)}