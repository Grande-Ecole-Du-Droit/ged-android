package com.upsaclay.authentication.presentation.registration.first

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.upsaclay.authentication.R
import com.upsaclay.common.domain.extensions.uppercaseFirstLetter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FirstRegistrationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FirstRegistrationUiState())
    internal val uiState: StateFlow<FirstRegistrationUiState> = _uiState

    fun onFirstNameChange(firstName: String) {
        if (validateName(firstName)) {
            _uiState.update {
                it.copy(firstName = firstName)
            }
        }
    }

    fun onLastNameChange(lastName: String) {
        if (validateName(lastName)) {
            _uiState.update {
                it.copy(lastName = lastName)
            }
        }
    }

    fun validateInputs(): Boolean {
        val (firstName, lastName) = _uiState.value

        _uiState.update {
            it.copy(
                firstName = firstName.trim().uppercaseFirstLetter(),
                lastName = lastName.trim().uppercaseFirstLetter(),
                firstNameError = R.string.mandatory_field.takeIf { firstName.isBlank() },
                lastNameError = R.string.mandatory_field.takeIf { lastName.isBlank() }
            )
        }

        return with(_uiState.value) {
            firstNameError == null && lastNameError == null
        }
    }

    private fun validateName(name: String): Boolean = name.all { it.isLetter() }

    internal data class FirstRegistrationUiState(
        val firstName: String = "",
        val lastName: String = "",
        @StringRes val firstNameError: Int? = null,
        @StringRes val lastNameError: Int? = null
    )
}