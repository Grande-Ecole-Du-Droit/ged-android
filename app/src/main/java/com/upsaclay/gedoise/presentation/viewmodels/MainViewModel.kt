package com.upsaclay.gedoise.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.DataListeningUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val dataListeningUseCase: DataListeningUseCase,
    private val clearDataUseCase: ClearDataUseCase
): ViewModel() {
    fun startListening() {
        updateDataListening()
        checkCurrentUser()
    }

    private fun updateDataListening() {
        viewModelScope.launch {
            authenticationRepository.isAuthenticated
                .filterNotNull()
                .collectLatest {
                    if (it) {
                        dataListeningUseCase.start()
                    } else {
                        dataListeningUseCase.stop()
                        delay(2000)
                        clearDataUseCase()
                    }
                }
        }
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            userRepository.user
                .filterNotNull()
                .take(1)
                .collect { currentUser ->
                    userRepository.getUser(currentUser.id)?.let { remoteUser ->
                        if (remoteUser != currentUser) {
                            userRepository.setCurrentUser(remoteUser)
                        }
                    } ?: run {
                        authenticationRepository.logout()
                        userRepository.deleteCurrentUser()
                    }
                }
        }
    }
}