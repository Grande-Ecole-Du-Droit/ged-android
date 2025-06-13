package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ResendAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val scope: CoroutineScope
) {
    operator fun invoke(announcement: Announcement) {
        if (!connectivityObserver.isConnected) {
            throw NoInternetConnectionException()
        }
        scope.launch {
            try {
                announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
                announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
            } catch (_: Exception) {
                announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
            }
        }
    }
}