package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AuthenticationRepositoryImpl(
    private val firebaseAuthenticationRepository: FirebaseAuthenticationRepository,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    scope: CoroutineScope
) : AuthenticationRepository {
    private val _authenticated = authenticationLocalDataSource.getAuthenticationState()
        .map { it && firebaseAuthenticationRepository.isAuthenticated() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    override val authenticated: Flow<Boolean> = _authenticated.filterNotNull()
    override val isAuthenticated: Boolean
        get() = _authenticated.value ?: false

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