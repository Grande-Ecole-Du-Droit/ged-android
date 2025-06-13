package com.upsaclay.news.data.remote

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.toAnnouncement
import com.upsaclay.news.data.toRemote
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnnouncementRemoteDataSource(private val announcementApi: AnnouncementApi) {
    suspend fun getAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        mapServerResponseException(
            block = { announcementApi.getAnnouncements() }
        )?.map { it.toAnnouncement() } ?: emptyList()
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { announcementApi.createAnnouncement(announcement.toRemote()) }
            )
        }
    }

    suspend fun deleteAnnouncement(id: String) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { announcementApi.deleteAnnouncement(id) }
            )
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { announcementApi.updateAnnouncement(announcement.toRemote()) }
            )
        }
    }
}