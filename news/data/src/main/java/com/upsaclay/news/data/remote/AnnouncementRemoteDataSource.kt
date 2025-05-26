package com.upsaclay.news.data.remote

import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.toAnnouncement
import com.upsaclay.news.data.toRemote
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AnnouncementRemoteDataSource(private val announcementApi: AnnouncementApi) {
    suspend fun getAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        val response = announcementApi.getAnnouncements()
        if (response.isSuccessful) {
            response.body()?.map { it.toAnnouncement() } ?: emptyList()
        } else {
            val errorMessage = formatHttpError(response)
            throw InternalServerException(errorMessage)
        }
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            val response = announcementApi.createAnnouncement(announcement.toRemote())
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError(response)
                throw InternalServerException(errorMessage)
            }
        }
    }

    suspend fun deleteAnnouncement(id: String) {
        withContext(Dispatchers.IO) {
            val response = announcementApi.deleteAnnouncement(id)
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError(response)
                throw InternalServerException(errorMessage)
            }
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            val response = announcementApi.updateAnnouncement(announcement.toRemote())
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError(response)
                throw InternalServerException(errorMessage)
            }
        }
    }
}