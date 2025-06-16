package com.upsaclay.news.presentation.announcement.editannouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAnnouncementViewModel(
    private val announcement: Announcement,
    private val updateAnnouncementUseCase: UpdateAnnouncementUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(
        EditAnnouncementUiState(
            title = announcement.title ?: "",
            content = announcement.content,
        )
    )
    internal val uiState: StateFlow<EditAnnouncementUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                updateEnabled = validateUpdate(title, _uiState.value.content)
            )
        }
    }

    fun onContentChange(content: String) {
        _uiState.update {
            it.copy(
                content = content,
                updateEnabled = validateUpdate(_uiState.value.title, content)
            )
        }
    }

    fun updateAnnouncement() {
        if (!validateUpdate(_uiState.value.title, _uiState.value.content)) {
            return
        }

        val updatedAnnouncement = announcement.copy(
            title = _uiState.value.title.trim(),
            content = _uiState.value.content.trim()
        )

        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            try {
                updateAnnouncementUseCase(updatedAnnouncement)
                _event.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun validateUpdate(title: String, content: String): Boolean {
        return validateTitle(title) || validateContent(content)
    }

    private fun validateTitle(title: String): Boolean {
        return title != announcement.title &&
                title.isNotBlank() &&
                _uiState.value.content.isNotBlank()
    }

    private fun validateContent(content: String): Boolean {
        return content != announcement.content && content.isNotBlank()
    }

    data class EditAnnouncementUiState(
        val title: String = "",
        val content: String = "",
        val loading: Boolean = false,
        val updateEnabled: Boolean = false
    )
}