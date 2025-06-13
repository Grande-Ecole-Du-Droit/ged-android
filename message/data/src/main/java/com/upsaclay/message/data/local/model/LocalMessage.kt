package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField

@Entity(tableName = MESSAGES_TABLE_NAME)
data class LocalMessage(
    @PrimaryKey
    @ColumnInfo(name = MessageField.MESSAGE_ID)
    val messageId: Long,
    @ColumnInfo(name = MessageField.SENDER_ID)
    val senderId: String,
    @ColumnInfo(name = MessageField.RECIPIENT_ID)
    val recipientId: String,
    @ColumnInfo(name = MessageField.CONVERSATION_ID)
    val conversationId: String,
    @ColumnInfo(name = MessageField.CONTENT)
    val content: String,
    @ColumnInfo(name = MessageField.TIMESTAMP)
    val messageTimestamp: Long,
    @ColumnInfo(name = MessageField.SEEN)
    val seen: Boolean,
    @ColumnInfo(name = MessageField.Local.STATE)
    val state: String
)