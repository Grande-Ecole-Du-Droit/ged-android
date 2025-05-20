package com.upsaclay.news.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import kotlinx.coroutines.flow.Flow
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
    internal val uiState: StateFlow<NewsUiState> = newsUiState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NewsUiState()
        )

    fun refreshAnnouncements() {
        viewModelScope.launch {
            refreshAnnouncementUseCase()
        }
    }

    fun recreateAnnouncement(announcement: Announcement) {
        recreateAnnouncementUseCase(announcement)
    }

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            deleteAnnouncementUseCase(announcement)
        }
    }

    private fun newsUiState(): Flow<NewsUiState> = combine(
        userRepository.user.filterNotNull(),
        announcementRepository.announcements,
        refreshAnnouncementUseCase.refreshing
    ) { user, announcements, refreshing ->
        NewsUiState(
            user = user,
            announcements = announcements
                .sortedBy { it.date }
                .map {
                    it.copy(
                        title = it.title?.take(100),
                        content = it.content.take(100)
                    )
                },
            refreshing = refreshing
        )
    }

    internal data class NewsUiState(
        val user: User? = null,
        val announcements: List<Announcement>? = null,
        val refreshing: Boolean = false
    )
}