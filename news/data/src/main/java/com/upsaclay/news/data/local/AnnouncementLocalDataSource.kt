package com.upsaclay.news.data.local

import com.upsaclay.news.data.AnnouncementMapper
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class AnnouncementLocalDataSource(private val announcementDao: AnnouncementDao) {
    fun getAnnouncements(): Flow<List<Announcement>> = announcementDao.getAnnouncements()
        .map { localAnnouncements ->
            localAnnouncements.map(AnnouncementMapper::toDomain)
        }

    suspend fun insertAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.insertAnnouncement(AnnouncementMapper.toLocal(announcement))
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.updateAnnouncement(AnnouncementMapper.toLocal(announcement))
        }
    }

    suspend fun upsertAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.upsertAnnouncement(AnnouncementMapper.toLocal(announcement))
        }
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            announcementDao.deleteAnnouncement(AnnouncementMapper.toLocal(announcement))
        }
    }
}