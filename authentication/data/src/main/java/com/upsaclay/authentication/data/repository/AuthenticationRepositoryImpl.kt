package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class AuthenticationRepositoryImpl(
    private val firebaseAuthenticationRepository: FirebaseAuthenticationRepository,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val scope: CoroutineScope
) : AuthenticationRepository {
    override val isAuthenticated = authenticationLocalDataSource.getAuthenticationState()

    init {
        scope.launch {
            if (!firebaseAuthenticationRepository.isAuthenticated()) {
                setAuthenticated(false)
            }
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        firebaseAuthenticationRepository.loginWithEmailAndPassword(email, password)
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String) {
        return firebaseAuthenticationRepository.registerWithEmailAndPassword(email, password)
    }

    override suspend fun logout() {
        scope.launch { firebaseAuthenticationRepository.logout() }
        setAuthenticated(false)
    }

    override suspend fun setAuthenticated(isAuthenticated: Boolean) {
        authenticationLocalDataSource.setAuthenticationState(isAuthenticated)
    }
}