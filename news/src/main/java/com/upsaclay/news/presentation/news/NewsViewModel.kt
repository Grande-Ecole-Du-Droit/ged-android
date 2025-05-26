package com.upsaclay.news.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(
    private val recreateAnnouncementUseCase: RecreateAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val refreshAnnouncementUseCase: RefreshAnnouncementUseCase,
    private val announcementRepository: AnnouncementRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val uiState: StateFlow<NewsUiState> = newsUiState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NewsUiState()
        )
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun refreshAnnouncements() {
        viewModelScope.launch {
            try {
                refreshAnnouncementUseCase()
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun recreateAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                recreateAnnouncementUseCase(announcement)
                _event.emit(SingleUiEvent.Success(R.string.announcement_created))
            } catch (e: Exception) {
                viewModelScope.launch {
                    _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
                }
            }
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                deleteAnnouncementUseCase(announcement)
                _event.emit(SingleUiEvent.Success(R.string.announcement_deleted))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            }
        }
    }

    private fun newsUiState(): Flow<NewsUiState> = combine(
        userRepository.user.filterNotNull(),
        announcementRepository.announcements,
        refreshAnnouncementUseCase.refreshing
    ) { user, announcements, refreshing ->
        NewsUiState(
            user = user,
            announcements = announcements.map {
                it.copy(
                    title = it.title?.takeIf { it.isNotBlank() }?.take(100),
                    content = it.content.take(100)
                )
            },
            refreshing = refreshing
        )
    }

    private fun mapErrorMessage(e: Exception): Int {
        return when (e) {
            is NoInternetConnectionException -> com.upsaclay.common.R.string.no_internet_connection
            else -> R.string.announcement_refresh_error
        }
    }

    data class NewsUiState(
        val user: User? = null,
        val announcements: List<Announcement>? = null,
        val refreshing: Boolean = false
    )
}