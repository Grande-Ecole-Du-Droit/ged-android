package com.upsaclay.news.presentation.announcement.createannouncement

import androidx.lifecycle.ViewModel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateAnnouncementViewModel(
    userRepository: UserRepository,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateAnnouncementUiState())
    internal val uiState : StateFlow<CreateAnnouncementUiState> = _uiState
    private val user: User? = userRepository.currentUser

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                createEnabled = validateCreate()
            )
        }
    }

    fun onContentChange(content: String) {
        _uiState.update {
            it.copy(
                content = content,
                createEnabled = validateCreate()
            )
        }
    }

    fun createAnnouncement() {
        if (user == null) return
        val (title, content) = _uiState.value
        val announcement = Announcement(
            id = GenerateIdUseCase.stringId,
            title = if (title.isBlank()) null else title.trim(),
            content = content.trim(),
            date = LocalDateTime.now(ZoneOffset.UTC),
            author = user,
            state = AnnouncementState.DRAFT
        )
        createAnnouncementUseCase(announcement)
    }

    private fun validateCreate(): Boolean = uiState.value.content.isNotBlank()

    internal data class CreateAnnouncementUiState(
        val title: String = "",
        val content: String = "",
        val createEnabled: Boolean = false
    )
}