package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.model.MessageField

private const val TABLE_NAME = "conversation_message"

@Entity(tableName = TABLE_NAME)
data class LocalConversationMessage(
    @PrimaryKey
    @ColumnInfo(name = ConversationField.CONVERSATION_ID) val conversationId: String,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_ID) val interlocutorId: String,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_FIRST_NAME) val interlocutorFirstName: String,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_LAST_NAME) val interlocutorLastName: String,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_EMAIL) val interlocutorEmail: String,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_SCHOOL_LEVEL) val interlocutorSchoolLevel: String,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_IS_MEMBER) val interlocutorIsMember: Boolean,
    @ColumnInfo(name = ConversationField.Local.INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME) val interlocutorProfilePictureFileName: String?,
    @ColumnInfo(name = ConversationField.CREATED_AT) val createdAt: Long,
    @ColumnInfo(name = ConversationField.Local.CONVERSATION_STATE) val conversationState: String,
    @ColumnInfo(name = ConversationField.Local.CONVERSATION_DELETE_TIME) val conversationDeleteTime: Long?,

    @ColumnInfo(name = MessageField.MESSAGE_ID) val messageId: Long,
    @ColumnInfo(name = MessageField.SENDER_ID) val senderId: String,
    @ColumnInfo(name = MessageField.RECIPIENT_ID) val recipientId: String,
    @ColumnInfo(name = MessageField.CONTENT) val content: String,
    @ColumnInfo(name = MessageField.TIMESTAMP) val messageTimestamp: Long,
    @ColumnInfo(name = MessageField.SEEN) val seen: Boolean,
    @ColumnInfo(name = MessageField.Local.STATE) val messageState: String
)

