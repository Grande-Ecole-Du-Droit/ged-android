package com.upsaclay.gedoise.presentation.profile.account

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.DeleteProfilePictureUseCase
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.gedoise.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
    private val deleteProfilePictureUseCase: DeleteProfilePictureUseCase,
    private val connectivityObserver: ConnectivityObserver,
    userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        userRepository.user
            .filterNotNull()
            .map(::updateState)
            .launchIn(viewModelScope)
    }

    fun updateProfilePicture() {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                val user = requireNotNull(_uiState.value.user)
                _uiState.value.profilePictureUri?.let { uri ->
                    updateState(loading = true)
                    updateProfilePictureUseCase(user, uri)
                    updateState(loading = false, screenState = AccountScreenState.READ)
                    _event.emit(SingleUiEvent.Success(R.string.profile_picture_updated))
                }
            } catch (e: Exception) {
                updateState(loading = false)
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun deleteProfilePicture() {
        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                val user = requireNotNull(_uiState.value.user)
                updateState(loading = true)
                user.profilePictureFileName?.let {
                    deleteProfilePictureUseCase(user.id, it)
                }
                resetValues()
                _event.emit(SingleUiEvent.Success(R.string.profile_picture_deleted))
            } catch (e: Exception) {
                updateState(loading = false)
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            }
        }
    }

    fun onScreenStateChange(screenState: AccountScreenState) {
        updateState(screenState = screenState)
    }

    fun resetValues() {
        updateState(
            screenState = AccountScreenState.READ,
            profilePictureUri = null,
            loading = false
        )
    }

    fun onProfilePictureUriChange(uri: Uri?) {
        updateState(profilePictureUri = uri)

    }

    private fun updateState(
        user: User? = _uiState.value.user,
        profilePictureUri: Uri? = _uiState.value.profilePictureUri,
        loading: Boolean = _uiState.value.loading,
        screenState: AccountScreenState = _uiState.value.screenState
    ) {
        _uiState.update {
            it.copy(
                user = user,
                profilePictureUri = profilePictureUri,
                loading = loading,
                screenState = screenState,
            )
        }
    }

    private fun mapErrorMessage(e: Exception): Int {
        return mapNetworkErrorMessage(e) {
            when (e) {
                is IllegalArgumentException -> com.upsaclay.common.R.string.user_not_found
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    data class AccountUiState(
        val user: User? = null,
        val profilePictureUri: Uri? = null,
        val loading: Boolean = false,
        val screenState: AccountScreenState = AccountScreenState.READ,
    )
}

enum class AccountScreenState {
    READ,
    EDIT
}