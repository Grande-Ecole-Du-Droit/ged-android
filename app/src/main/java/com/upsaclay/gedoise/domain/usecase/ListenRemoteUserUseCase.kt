package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class ListenRemoteUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val scope: CoroutineScope
) {
    internal var job: Job? = null

    fun start() {
        job = scope.launch {
            userRepository.user.filterNotNull().take(1).collect { user ->
                userRepository.getUserFlow(user.id).collect {
                    if (it == null) {
                        authenticationRepository.logout()
                    } else {
                        userRepository.storeUser(it)
                    }
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}