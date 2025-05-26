package com.upsaclay.news.data.repository

import com.upsaclay.common.data.exceptions.handleNetworkException
import com.upsaclay.news.data.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class AnnouncementRepositoryImpl(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val announcementLocalDataSource: AnnouncementLocalDataSource,
    scope: CoroutineScope
) : AnnouncementRepository {
    private val _announcements = announcementLocalDataSource.getAnnouncements()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    override val announcements: Flow<List<Announcement>> = _announcements

    init {
        scope.launch {
            refreshAnnouncements()
        }
    }

    override fun getAnnouncementFlow(announcementId: String): Flow<Announcement?> =
        _announcements.map { announcements ->
            announcements.firstOrNull { it.id == announcementId }
        }

    override fun getAnnouncement(announcementId: String): Announcement? =
        _announcements.value.firstOrNull { it.id == announcementId }

    override suspend fun refreshAnnouncements() {
        val remoteAnnouncements = runCatching {
            announcementRemoteDataSource.getAnnouncement()
        }.getOrElse { return }

        val announcementsToDelete = _announcements.value
            .filter { it.state == AnnouncementState.PUBLISHED }
            .filterNot { remoteAnnouncements.contains(it) }
        announcementsToDelete.forEach { announcementLocalDataSource.deleteAnnouncement(it) }

        val announcementsToUpsert = remoteAnnouncements
            .filterNot { _announcements.value.contains(it) }
        announcementsToUpsert.forEach { announcementLocalDataSource.upsertAnnouncement(it) }
    }

    override suspend fun createRemoteAnnouncement(announcement: Announcement) {
        handleNetworkException(
            message = "Failed to create remote announcement",
            block = { announcementRemoteDataSource.createAnnouncement(announcement) }
        )
    }

    override suspend fun createLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.upsertAnnouncement(announcement)
    }

    override suspend fun updateAnnouncement(announcement: Announcement) {
        handleNetworkException(
            message = "Failed to update announcement",
            block = {
                announcementRemoteDataSource.updateAnnouncement(announcement)
                announcementLocalDataSource.upsertAnnouncement(announcement)
            }
        )
    }

    override suspend fun updateLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.upsertAnnouncement(announcement)
    }

    override suspend fun deleteAnnouncement(announcement: Announcement) {
        handleNetworkException(
            message = "Failed to delete announcement",
            block = {
                announcementRemoteDataSource.deleteAnnouncement(announcement.id)
                announcementLocalDataSource.deleteAnnouncement(announcement)
            }
        )
    }

    override suspend fun deleteLocalAnnouncement(announcement: Announcement) {
        announcementLocalDataSource.deleteAnnouncement(announcement)
    }
}