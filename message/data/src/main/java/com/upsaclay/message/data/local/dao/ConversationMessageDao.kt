package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
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
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField.CONTENT
import com.upsaclay.message.data.model.MessageField.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.SEEN
import com.upsaclay.message.data.model.MessageField.SENDER_ID
import com.upsaclay.message.data.model.MessageField.TIMESTAMP
import kotlinx.coroutines.flow.Flow
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_ID as CONVERSATION_CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.CONVERSATION_ID as MESSAGE_CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.STATE as MESSAGE_STATE

@Dao
interface ConversationMessageDao {
    @Transaction
    @Query("""
        SELECT C.$CONVERSATION_CONVERSATION_ID,
           C.$INTERLOCUTOR_ID, 
           C.$INTERLOCUTOR_FIRST_NAME,
           C.$INTERLOCUTOR_LAST_NAME, 
           C.$INTERLOCUTOR_EMAIL, 
           C.$INTERLOCUTOR_SCHOOL_LEVEL,
           C.$INTERLOCUTOR_IS_MEMBER,
           C.$INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME,
           C.$CREATED_AT,
           C.$CONVERSATION_STATE, 
           C.$CONVERSATION_DELETE_TIME,
           M.$MESSAGE_ID, 
           M.$SENDER_ID,
           M.$RECIPIENT_ID,
           M.$CONTENT,
           M.$TIMESTAMP,
           M.$SEEN, 
           M.$MESSAGE_STATE
        FROM $CONVERSATIONS_TABLE_NAME C
        JOIN $MESSAGES_TABLE_NAME M ON C.$CONVERSATION_CONVERSATION_ID = M.$MESSAGE_CONVERSATION_ID
        JOIN (
            SELECT $MESSAGE_CONVERSATION_ID, MAX($TIMESTAMP) AS MAX_TIMESTAMP
            FROM $MESSAGES_TABLE_NAME
            GROUP BY $MESSAGE_CONVERSATION_ID
        ) M_MAX
          ON M.$MESSAGE_CONVERSATION_ID = M_MAX.$MESSAGE_CONVERSATION_ID
          AND M.$TIMESTAMP = M_MAX.MAX_TIMESTAMP
          ORDER BY M_MAX.MAX_TIMESTAMP DESC
    """)
    fun getConversationsMessage(): Flow<List<LocalConversationMessage>>
}