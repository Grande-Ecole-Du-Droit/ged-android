package com.upsaclay.message.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.CREATED_AT
import com.upsaclay.message.data.model.ConversationField.DELETE_TIME
import com.upsaclay.message.data.model.ConversationField.Remote.PARTICIPANTS

internal data class RemoteConversation(
    @get:PropertyName(CONVERSATION_ID)
    @set:PropertyName(CONVERSATION_ID)
    var conversationId: String = "",

    @get:PropertyName(PARTICIPANTS)
    @set:PropertyName(PARTICIPANTS)
    var participants: List<String> = emptyList(),

    @get:PropertyName(CREATED_AT)
    @set:PropertyName(CREATED_AT)
    var createdAt: Timestamp = Timestamp.now(),

    @get:PropertyName(DELETE_TIME)
    @set:PropertyName(DELETE_TIME)
    var deleteTime: Map<String, Timestamp>? = null
)