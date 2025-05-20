package com.upsaclay.authentication.data.repository.firebase

import com.google.firebase.auth.FirebaseAuthException
import com.upsaclay.authentication.data.api.FirebaseAuthenticationApi
import com.upsaclay.authentication.domain.entity.exception.AuthErrorCode
import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.common.data.exceptions.handleNetworkException
import com.upsaclay.common.domain.entity.DuplicateDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class FirebaseAuthenticationRepositoryImpl(
    private val firebaseAuthenticationApi: FirebaseAuthenticationApi
): FirebaseAuthenticationRepository {
    override fun isAuthenticated(): Boolean = firebaseAuthenticationApi.isAuthenticated()

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        withContext(Dispatchers.IO) {
            handleNetworkException(
                message = "Failed to login with email and password",
                block = { firebaseAuthenticationApi.signInWithEmailAndPassword(email, password) },
                catchSpecificException = ::handleAuthException
            )
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String) {
        withContext(Dispatchers.IO) {
            handleNetworkException(
                message = "Failed to register with email and password",
                block = { firebaseAuthenticationApi.signUpWithEmailAndPassword(email, password) },
                catchSpecificException = ::handleAuthException
            )
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            handleNetworkException(
                message = "Failed to logout",
                block = { firebaseAuthenticationApi.signOut() }
            )
        }
    }

    private fun handleAuthException(e: Exception) {
        if (e is FirebaseAuthException) {
            when (AuthErrorCode.fromCode(e.errorCode)) {
                AuthErrorCode.EMAIL_ALREADY_AFFILIATED -> throw DuplicateDataException()
                AuthErrorCode.INVALID_CREDENTIALS -> throw InvalidCredentialsException()
                else -> throw IOException()
            }
        } else {
            throw IOException()
        }
    }
}