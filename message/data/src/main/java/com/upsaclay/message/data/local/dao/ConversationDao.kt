package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_STATE
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME")
    fun getConversationsFlow(): Flow<List<LocalConversation>>

    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME")
    suspend fun getConversations(): List<LocalConversation>

    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME WHERE $INTERLOCUTOR_ID = :interlocutorId")
    fun getConversationFlow(interlocutorId: String): Flow<LocalConversation?>

    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME WHERE $INTERLOCUTOR_ID = :interlocutorId")
    suspend fun getConversation(interlocutorId: String): LocalConversation?

    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME WHERE $CONVERSATION_STATE = 'CREATING'")
    suspend fun getUnCreateConversations(): List<LocalConversation>

    @Insert
    suspend fun insertConversation(localConversation: LocalConversation)

    @Update
    suspend fun updateConversation(localConversation: LocalConversation)

    @Upsert
    suspend fun upsertConversation(localConversation: LocalConversation)

    @Query("DELETE FROM $CONVERSATIONS_TABLE_NAME")
    suspend fun deleteConversations()
}