package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.withTimeout

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val connectivityObserver: ConnectivityObserver
) {
    suspend operator fun invoke(email: String, password: String) {
        if (!connectivityObserver.isConnected) {
            throw NoInternetConnectionException()
        }

        withTimeout(10000) {
            authenticationRepository.loginWithEmailAndPassword(email, password)
            userRepository.getUserWithEmail(email)?.let {
                userRepository.storeUser(it)
                authenticationRepository.setAuthenticated(true)
            } ?: throw InvalidCredentialsException()
        }
    }
}