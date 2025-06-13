package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

private const val DEBOUNCE_INTERVAL = 10000L

class RefreshAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val connectivityObserver: ConnectivityObserver
) {
    internal var lastRequestTime: Long = 0
    private val _refreshing = MutableStateFlow(false)
    val refreshing: Flow<Boolean> = _refreshing

    suspend operator fun invoke() {
        if (!connectivityObserver.isConnected) {
            delay(1500)
            _refreshing.emit(false)
            throw NoInternetConnectionException()
        }

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            _refreshing.emit(true)
            announcementRepository.refreshAnnouncements()
            lastRequestTime = currentTime
        }

        _refreshing.emit(false)
    }
}