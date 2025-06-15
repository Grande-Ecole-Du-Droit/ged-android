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
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(
    private val resendAnnouncementUseCase: ResendAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val refreshAnnouncementUseCase: RefreshAnnouncementUseCase,
    private val announcementRepository: AnnouncementRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        initUiState()
    }

    private fun initUiState() {
        combine(
            userRepository.user,
            announcementRepository.announcements
        ) { user, announcements ->
            _uiState.update {
                NewsUiState(
                    user = user,
                    announcements = announcements.map {
                        it.copy(
                            title = it.title?.takeIf { it.isNotBlank() }?.take(100),
                            content = it.content.take(100)
                        )
                    }
                )
            }
        }.launchIn(viewModelScope)
    }

    fun refreshAnnouncements() {
        _uiState.update { it.copy(refreshing = true) }
        viewModelScope.launch {
            try {
                refreshAnnouncementUseCase()
                _uiState.update { it.copy(refreshing = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(refreshing = false) }
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun resendAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            try {
                resendAnnouncementUseCase(announcement)
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