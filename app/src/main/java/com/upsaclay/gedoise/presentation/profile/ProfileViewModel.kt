package com.upsaclay.gedoise.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetCurrentUserFlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authenticationRepository: com.upsaclay.authentication.domain.repository.AuthenticationRepository,
    getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase
) : ViewModel() {
    val user: Flow<com.upsaclay.common.domain.model.User?> = getCurrentUserFlowUseCase()

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
    fun contactSupport()
    {

    }
}