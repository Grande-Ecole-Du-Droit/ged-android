package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AuthenticationRepositoryImpl(
    private val firebaseAuthenticationRepository: FirebaseAuthenticationRepository,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
) : AuthenticationRepository {
    override val isAuthenticated: Flow<Boolean> = authenticationLocalDataSource.getAuthenticationState()
        .map { it && firebaseAuthenticationRepository.isAuthenticated() }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        firebaseAuthenticationRepository.loginWithEmailAndPassword(email, password)
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String =
        firebaseAuthenticationRepository.registerWithEmailAndPassword(email, password)

    override suspend fun logout() {
        firebaseAuthenticationRepository.logout()
        setAuthenticated(false)
    }

    override suspend fun setAuthenticated(isAuthenticated: Boolean) {
        authenticationLocalDataSource.setAuthenticationState(isAuthenticated)
    }
}