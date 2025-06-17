package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.delay

private const val DEBOUNCE_INTERVAL = 10000L

class RefreshAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val connectivityObserver: ConnectivityObserver
) {
    internal var lastRequestTime: Long = 0

    suspend operator fun invoke() {
        if (!connectivityObserver.isConnected) {
            delay(1500)
            throw NoInternetConnectionException()
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            announcementRepository.refreshAnnouncements()
            lastRequestTime = currentTime
        }
    }
}