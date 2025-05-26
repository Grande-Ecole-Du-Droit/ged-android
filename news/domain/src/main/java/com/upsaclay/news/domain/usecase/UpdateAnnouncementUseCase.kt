package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository

class UpdateAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val connectivityObserver: ConnectivityObserver
) {
    suspend operator fun invoke(announcement: Announcement) {
        if (!connectivityObserver.isConnected) {
            throw NoInternetConnectionException()
        }
        announcementRepository.updateAnnouncement(announcement)
    }
}