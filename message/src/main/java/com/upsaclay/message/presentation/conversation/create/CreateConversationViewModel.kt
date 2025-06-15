package com.upsaclay.message.presentation.conversation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateConversationViewModel(
    private val userRepository: UserRepository,
    private val getConversationUseCase: GetConversationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateConversationUiState())
    val uiState: StateFlow<CreateConversationUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private var defaultUsers: List<User> = emptyList()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            try {
                userRepository.getUsers()
                    .filter { it.id != userRepository.currentUser?.id }
                    .also { users ->
                        _uiState.update {
                            it.copy(users = users, loading = false)
                        }
                        defaultUsers = users
                    }
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    suspend fun getConversation(interlocutor: User): Conversation? {
        return try {
            getConversationUseCase(interlocutor)
        } catch (e: Exception) {
            _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            null
        }
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

    fun resetQuery() {
        _uiState.update {
            it.copy(
                query = "",
                users = defaultUsers
            )
        }
    }

    private fun getFilteredUsers(query: String): List<User> {
        return query.takeIf { it.isNotBlank() }?.let {
            defaultUsers.filter { user ->
                user.firstName.contains(query, ignoreCase = true) ||
                        user.lastName.contains(query, ignoreCase = true)
            }
        } ?: defaultUsers
    }

    private fun mapErrorMessage(e: Throwable): Int {
        return mapNetworkErrorMessage(e) {
            when (e) {
                is IllegalArgumentException -> com.upsaclay.common.R.string.current_user_not_found
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    data class CreateConversationUiState(
        val users: List<User> = emptyList(),
        val query: String = "",
        val loading: Boolean = true
    )
}