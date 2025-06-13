package com.upsaclay.news.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.upsaclay.news.data.AnnouncementField.Local.DATE
import com.upsaclay.news.data.local.model.ANNOUNCEMENTS_TABLE
import com.upsaclay.news.data.local.model.LocalAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM $ANNOUNCEMENTS_TABLE ORDER BY $DATE DESC")
    fun getAnnouncements(): Flow<List<LocalAnnouncement>>

    @Upsert
    suspend fun upsertAnnouncement(localAnnouncement: LocalAnnouncement)

    @Delete
    suspend fun deleteAnnouncement(localAnnouncement: LocalAnnouncement)
}