package com.upsaclay.message.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField.CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.STATE
import com.upsaclay.message.data.model.MessageField.RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.SEEN
import com.upsaclay.message.data.model.MessageField.TIMESTAMP
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE $CONVERSATION_ID = :conversationId 
        ORDER BY $TIMESTAMP DESC
    """)
    fun getMessages(conversationId: String): PagingSource<Int, LocalMessage>

    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE $CONVERSATION_ID = :conversationId 
        ORDER BY $TIMESTAMP DESC
        LIMIT 1
    """)
    fun getLastMessageFlow(conversationId: String): Flow<LocalMessage?>

    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE ${CONVERSATION_ID} = :conversationId 
        ORDER BY $TIMESTAMP DESC
        LIMIT 1
    """)
    suspend fun getLastMessage(conversationId: String): LocalMessage?

    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE $CONVERSATION_ID = :conversationId 
        AND $RECIPIENT_ID == :userId
        AND $SEEN = 0
    """)
    suspend fun getUnreadMessagesByUser(conversationId: String, userId: String): List<LocalMessage>

    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE $STATE = 'SENDING'
    """)
    suspend fun getUnsentMessages(): List<LocalMessage>

    @Insert
    fun insertMessage(localMessage: LocalMessage)

    @Update
    suspend fun updateMessage(localMessage: LocalMessage)

    @Query("""
        UPDATE $MESSAGES_TABLE_NAME
        SET $SEEN = 1
        WHERE $CONVERSATION_ID = :conversationId
        AND $RECIPIENT_ID = :userId
        AND $SEEN = 0
    """)
    suspend fun updateSeenMessages(conversationId: String, userId: String)

    @Upsert
    suspend fun upsertMessage(localMessage: LocalMessage)

    @Delete
    suspend fun deleteMessage(localMessage: LocalMessage)

    @Query("DELETE FROM $MESSAGES_TABLE_NAME WHERE $CONVERSATION_ID = :conversationId")
    suspend fun deleteMessages(conversationId: String)

    @Query("DELETE FROM $MESSAGES_TABLE_NAME")
    suspend fun deleteAllMessages()
}