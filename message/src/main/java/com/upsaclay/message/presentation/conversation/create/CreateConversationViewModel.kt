package com.upsaclay.message.presentation.conversation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserNotFoundException
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateConversationViewModel(
    private val conversationRepository: ConversationRepository,
    private val createConversationUseCase: CreateConversationUseCase,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateConversationUiState())
    val uiState: StateFlow<CreateConversationUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private var defaultUsers: List<User> = emptyList()

    init {
        fetchUsers()
    }

    suspend fun getConversation(interlocutor: User): Conversation? {
        return runCatching {
            conversationRepository.getLocalConversation(interlocutor.id) ?: run {
                val user = requireNotNull(userRepository.currentUser)
                createConversationUseCase.generateNewConversation(user.id, interlocutor)
            }
        }
            .onFailure { _event.emit(SingleUiEvent.Error(mapErrorMessage(it))) }
            .getOrNull()
    }

    fun onQueryChange(userName: String) {
        _uiState.update {
            it.copy(query = userName)
        }

        getFilteredUsers(userName).also { users ->
            _uiState.update {
                it.copy(users = users)
            }
        }
    }

    private fun fetchUsers() {
        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            userRepository.getUsers()
                .filter { it.id != userRepository.currentUser?.id }
                .also { users ->
                    _uiState.update {
                        it.copy(users = users, loading = false)
                    }
                    defaultUsers = users
                }
        }
    }

    private fun getFilteredUsers(query: String): List<User> {
       return if (query.isBlank()) {
            defaultUsers
        } else {
           defaultUsers.filter {
               it.firstName.contains(query, ignoreCase = true) ||
                       it.lastName.contains(query, ignoreCase = true)
           }
        }
    }

    private fun mapErrorMessage(e: Throwable): Int {
        return when (e) {
            is IllegalArgumentException -> com.upsaclay.common.R.string.user_not_found
            else -> com.upsaclay.common.R.string.unknown_error
        }
    }

    data class CreateConversationUiState(
        val users: List<User> = emptyList(),
        val query: String = "",
        val loading: Boolean = true
    )
}