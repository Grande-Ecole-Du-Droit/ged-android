package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_DELETE_TIME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_STATE
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_EMAIL
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_FIRST_NAME
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_ID
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_IS_MEMBER
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_LAST_NAME
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_SCHOOL_LEVEL

@Entity(tableName = CONVERSATIONS_TABLE_NAME)
data class LocalConversation(
    @PrimaryKey
    @ColumnInfo(name = CONVERSATION_ID)
    val conversationId: String,
    @ColumnInfo(name = INTERLOCUTOR_ID)
    val interlocutorId: String,
    @ColumnInfo(name = INTERLOCUTOR_FIRST_NAME)
    val interlocutorFirstName: String,
    @ColumnInfo(name = INTERLOCUTOR_LAST_NAME)
    val interlocutorLastName: String,
    @ColumnInfo(name = INTERLOCUTOR_EMAIL)
    val interlocutorEmail: String,
    @ColumnInfo(name = INTERLOCUTOR_SCHOOL_LEVEL)
    val interlocutorSchoolLevel: String,
    @ColumnInfo(name = INTERLOCUTOR_IS_MEMBER)
    val interlocutorIsMember: Boolean,
    @ColumnInfo(name = INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME)
    val interlocutorProfilePictureFileName: String?,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long,
    @ColumnInfo(name = CONVERSATION_STATE)
    val conversationState: String,
    @ColumnInfo(name = CONVERSATION_DELETE_TIME)
    val conversationDeleteTime: Long?
)