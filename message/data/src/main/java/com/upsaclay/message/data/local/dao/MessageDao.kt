package com.upsaclay.message.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE ${MessageField.CONVERSATION_ID} = :conversationId 
        ORDER BY ${MessageField.TIMESTAMP} DESC
    """)
    fun getMessages(conversationId: String): PagingSource<Int, LocalMessage>

    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE ${MessageField.CONVERSATION_ID} = :conversationId 
        ORDER BY ${MessageField.TIMESTAMP} DESC
        LIMIT 1
    """)
    fun getLastMessage(conversationId: String): Flow<LocalMessage>

    @Query("""
        SELECT * FROM $MESSAGES_TABLE_NAME
        WHERE ${MessageField.CONVERSATION_ID} = :conversationId 
        AND ${MessageField.SEEN} = 0
        AND ${MessageField.RECIPIENT_ID} == :userId
        ORDER BY ${MessageField.TIMESTAMP} DESC
    """)
    fun getUnreadMessagesByUser(conversationId: String, userId: String): Flow<List<LocalMessage>>

    @Update
    suspend fun updateMessage(localMessage: LocalMessage)

    @Upsert
    suspend fun upsertMessage(localMessage: LocalMessage)

    @Query("DELETE FROM $MESSAGES_TABLE_NAME WHERE ${MessageField.CONVERSATION_ID} = :conversationId")
    suspend fun deleteMessages(conversationId: String)

    @Query("DELETE FROM $MESSAGES_TABLE_NAME")
    suspend fun deleteAllMessages()
}