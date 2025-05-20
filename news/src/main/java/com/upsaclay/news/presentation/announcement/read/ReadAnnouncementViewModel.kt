package com.upsaclay.news.presentation.announcement.read

import android.accounts.NetworkErrorException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class ReadAnnouncementViewModel(
    announcementId: String,
    userRepository: UserRepository,
    announcementRepository: AnnouncementRepository,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase
) : ViewModel() {
    private val loading = MutableStateFlow(false)
    internal val uiState: StateFlow<ReadAnnouncementUiState> = combine(
        announcementRepository.getAnnouncementFlow(announcementId).filterNotNull(),
        userRepository.user.filterNotNull(),
        loading,
        ReadAnnouncementViewModel::ReadAnnouncementUiState
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ReadAnnouncementUiState()
    )

    private val _singleUiEvent = MutableSharedFlow<SingleUiEvent>()
    val singleUiEvent: SharedFlow<SingleUiEvent> = _singleUiEvent

    fun deleteAnnouncement() {
        val announcement = uiState.value.announcement ?: return
        loading.update { true }

        viewModelScope.launch {
            try {
                deleteAnnouncementUseCase(announcement)
                _singleUiEvent.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _singleUiEvent.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            } finally {
                loading.update { false }
            }
        }
    }

    private fun mapErrorMessage(error: Exception): Int {
        return when (error) {
            is ConnectException -> com.upsaclay.common.R.string.server_connection_error
            is SocketTimeoutException -> com.upsaclay.common.R.string.timeout_error
            is InternalServerException -> com.upsaclay.common.R.string.internal_server_error
            is NetworkErrorException -> com.upsaclay.common.R.string.unknown_network_error
            else -> com.upsaclay.common.R.string.unknown_error
        }
    }

    internal data class ReadAnnouncementUiState(
        val announcement: Announcement? = null,
        val user: User? = null,
        val loading: Boolean = false
    )
}