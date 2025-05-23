package com.upsaclay.news.data.local

import com.upsaclay.news.data.AnnouncementMapper
import com.upsaclay.news.data.toAnnouncement
import com.upsaclay.news.data.toLocal
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class AnnouncementLocalDataSource(private val announcementDao: AnnouncementDao) {
    fun getAnnouncements(): Flow<List<Announcement>> = announcementDao.getAnnouncements()
        .map { localAnnouncements ->
            localAnnouncements.map { it.toAnnouncement() }
        }

    suspend fun insertAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.insertAnnouncement(announcement.toLocal())
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.updateAnnouncement(announcement.toLocal())
        }
    }

    suspend fun upsertAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.upsertAnnouncement(announcement.toLocal())
        }
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.deleteAnnouncement(announcement.toLocal())
        }
    }
}