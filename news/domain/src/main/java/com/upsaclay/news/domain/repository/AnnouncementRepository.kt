package com.upsaclay.news.domain.repository

import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    val announcements: Flow<List<Announcement>>

    fun getAnnouncementFlow(announcementId: String): Flow<Announcement?>

    fun getAnnouncement(announcementId: String): Announcement?

    suspend fun refreshAnnouncements()

    suspend fun createLocalAnnouncement(announcement: Announcement)

    suspend fun createRemoteAnnouncement(announcement: Announcement)

    suspend fun updateAnnouncement(announcement: Announcement)

    suspend fun updateLocalAnnouncement(announcement: Announcement)

    suspend fun deleteAnnouncement(announcement: Announcement)

    suspend fun deleteLocalAnnouncement(announcement: Announcement)
}