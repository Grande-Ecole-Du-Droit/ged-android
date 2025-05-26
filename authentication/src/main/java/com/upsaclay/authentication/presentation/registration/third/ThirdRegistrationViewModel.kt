package com.upsaclay.authentication.presentation.registration.third

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.common.domain.entity.DuplicateDataException
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import com.upsaclay.common.utils.mapNetworkErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MIN_PASSWORD_LENGTH = 8

class ThirdRegistrationViewModel(
    private val registerUseCase: RegisterUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ThirdRegistrationUiState())
    internal val uiState: StateFlow<ThirdRegistrationUiState> = _uiState

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        schoolLevel: String
    ) {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        if (!validateInputs(email, password)) return

        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            try {
                registerUseCase(email, password, firstName, lastName, schoolLevel)
                _event.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        _uiState.update {
            it.copy(
                emailError = validateEmail(email),
                passwordError = validatePassword(password)
            )
        }

        return with(_uiState.value) {
            emailError == null && passwordError == null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.mandatory_field
            password.length < MIN_PASSWORD_LENGTH -> R.string.password_length_error
            else -> null
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.mandatory_field
            !VerifyEmailFormatUseCase(email) -> R.string.incorrect_email_format_error
            else -> null
        }
    }

    private fun mapErrorMessage(e: Exception): Int {
        return mapNetworkErrorMessage(e) {
            when (it) {
                is ForbiddenException -> R.string.user_not_white_listed
                is DuplicateDataException -> R.string.email_already_associated
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    internal data class ThirdRegistrationUiState(
        val email: String = "",
        val password: String = "",
        @StringRes val emailError: Int? = null,
        @StringRes val passwordError: Int? = null,
        val loading: Boolean = false,
    )
}