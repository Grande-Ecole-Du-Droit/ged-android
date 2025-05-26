package com.upsaclay.gedoise.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.DataListeningUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val dataListeningUseCase: DataListeningUseCase,
    private val clearDataUseCase:  ClearDataUseCase
): ViewModel() {
    fun startListening() {
        updateDataListening()
    }

    private fun updateDataListening() {
        viewModelScope.launch {
            authenticationRepository.isAuthenticated.collectLatest {
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
}