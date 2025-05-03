package com.upsaclay.news.presentation.announcement.create

import androidx.lifecycle.ViewModel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

class CreateAnnouncementViewModel(
    userRepository: UserRepository,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateAnnouncementUiState())
    internal val uiState : StateFlow<CreateAnnouncementUiState> = _uiState
    private val user: User? = userRepository.currentUser

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun onContentChange(content: String) {
        _uiState.update {
            it.copy(content = content)
        }
    }

    fun createAnnouncement() {
        if (user == null) return
        val (title, content) = _uiState.value

        val announcement = Announcement(
            id = GenerateIdUseCase.stringId,
            title = if (title.isBlank()) null else title.trim(),
            content = content.trim(),
            date = LocalDateTime.now(),
            author = user,
            state = AnnouncementState.SENDING
        )

        createAnnouncementUseCase(announcement)
    }

    internal data class CreateAnnouncementUiState(
        val title: String = "",
        val content: String = ""
    )
}