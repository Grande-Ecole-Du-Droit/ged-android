package com.upsaclay.message.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationsUiUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val userRepository: UserRepository,
    private val getConversationsUiUseCase: GetConversationsUiUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenConversations()
    }

    fun deleteConversation(conversation: Conversation) {
        try {
            val user = requireNotNull(userRepository.currentUser)
            deleteConversationUseCase(conversation, user.id)
        } catch (e: Exception) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(mapToErrorMessage(e)))
            }
        }
    }

    private fun listenConversations() {
        viewModelScope.launch {
            getConversationsUiUseCase().collectLatest { conversations ->
                _uiState.update {
                    it.copy(conversations = conversations)
                }
            }
        }
    }

    private fun mapToErrorMessage(e: Throwable): Int {
        return mapNetworkErrorMessage(e) {
            when (e) {
                is IllegalArgumentException -> com.upsaclay.common.R.string.user_not_found
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    data class ConversationUiState(val conversations: List<ConversationUi>? = null)
}