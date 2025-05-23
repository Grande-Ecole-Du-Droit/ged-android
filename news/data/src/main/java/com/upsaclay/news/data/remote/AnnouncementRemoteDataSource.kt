package com.upsaclay.news.data.remote

import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.news.data.AnnouncementMapper
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.toAnnouncement
import com.upsaclay.news.data.toRemote
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber.Forest.e
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

internal class AnnouncementRemoteDataSource(private val announcementApi: AnnouncementApi) {
    suspend fun getAnnouncement(): List<Announcement> = withContext(Dispatchers.IO) {
        try {
            val response = announcementApi.getAnnouncements()
            if (response.isSuccessful) {
                val remoteAnnouncements = response.body().takeIf { it != null } ?: emptyList()
                remoteAnnouncements.map { it.toAnnouncement() }
            } else {
                val errorMessage = formatHttpError("Error getting remote announcements", response)
                e(errorMessage)
                throw IOException(errorMessage)
            }
        } catch (e: Exception) {
            e("Error getting remote announcements: ${e.message}", e)
            throw e
        }
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            val response = try {
                announcementApi.createAnnouncement(announcement.toRemote())
            }
            catch (e: ConnectException) {
                e("Error creating remote announcement: ${e.message}")
                throw ConnectException()
            }
            catch (e: SocketTimeoutException) {
                e("Error creating remote announcement: ${e.message}")
                throw SocketTimeoutException()
            }
            catch (e: Exception) {
                e("Error creating remote announcement: ${e.message}")
                throw IOException()
            }

            if (!response.isSuccessful) {
                val errorMessage = formatHttpError("Error creating remote announcement", response)
                e(errorMessage)
                throw IOException(errorMessage)
            }
        }
    }

    suspend fun deleteAnnouncement(id: String) {
        withContext(Dispatchers.IO) {
            val response = try {
                announcementApi.deleteAnnouncement(id)
            } catch (e: ConnectException) {
                e("Error deleting remote announcement: ${e.message}", e)
                throw ConnectException()
            }
            catch (e: SocketTimeoutException) {
                e("Error updating remote announcement: ${e.message}", e)
                throw SocketTimeoutException()
            }
            catch (e: Exception) {
                e("Error deleting remote announcement: ${e.message}", e)
                throw IOException()
            }

            if (!response.isSuccessful) {
                val errorMessage = formatHttpError("Error deleting remote announcement", response)
                e(errorMessage)
                throw InternalServerException(errorMessage)
            }
        }
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        withContext(Dispatchers.IO) {
            val response = try {
                announcementApi.updateAnnouncement(announcement.toRemote())
            } catch (e: ConnectException) {
                e("Error updating remote announcement: ${e.message}", e)
                throw ConnectException()
            }
            catch (e: SocketTimeoutException) {
                e("Error updating remote announcement: ${e.message}", e)
                throw SocketTimeoutException()
            }
            catch (e: Exception) {
                e("Error updating remote announcement: ${e.message}", e)
                throw IOException()
            }

            if (!response.isSuccessful) {
                val errorMessage = formatHttpError("Error updating remote announcement", response)
                e(errorMessage)
                throw InternalServerException(errorMessage)
            }
        }
    }
}