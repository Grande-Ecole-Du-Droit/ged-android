package com.upsaclay.message.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.upsaclay.message.data.model.MessageField

internal data class RemoteMessage(
    @get:PropertyName(MessageField.MESSAGE_ID)
    @set:PropertyName(MessageField.MESSAGE_ID)
    var messageId: Int = 0,

    @get:PropertyName(MessageField.CONVERSATION_ID)
    @set:PropertyName(MessageField.CONVERSATION_ID)
    var conversationId: String = "",

    @get:PropertyName(MessageField.SENDER_ID)
    @set:PropertyName(MessageField.SENDER_ID)
    var senderId: String = "",

    @get:PropertyName(MessageField.RECIPIENT_ID)
    @set:PropertyName(MessageField.RECIPIENT_ID)
    var recipientId: String = "",

    @get:PropertyName(MessageField.CONTENT)
    @set:PropertyName(MessageField.CONTENT)
    var content: String = "",

    @get:PropertyName(MessageField.TIMESTAMP)
    @set:PropertyName(MessageField.TIMESTAMP)
    var timestamp: Timestamp = Timestamp.now(),

    @get:PropertyName(MessageField.SEEN)
    @set:PropertyName(MessageField.SEEN)
    var seen: Boolean = false
)