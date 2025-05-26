package com.upsaclay.authentication.data.repository.firebase

import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.upsaclay.authentication.data.api.FirebaseAuthenticationApi
import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.common.data.exceptions.handleNetworkException
import com.upsaclay.common.domain.entity.DuplicateDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseAuthenticationRepositoryImpl(
    private val firebaseAuthenticationApi: FirebaseAuthenticationApi
): FirebaseAuthenticationRepository {
    override fun isAuthenticated(): Boolean = firebaseAuthenticationApi.isAuthenticated()

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        withContext(Dispatchers.IO) {
            handleNetworkException(
                message = "Failed to login with email and password",
                block = { firebaseAuthenticationApi.signIn(email, password) },
                mapSpecificException = ::mapAuthException
            )
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            handleNetworkException(
                message = "Failed to register with email and password",
                block = { firebaseAuthenticationApi.signUp(email, password) },
                mapSpecificException = ::mapAuthException
            )
        }
    }

    override fun logout() {
        firebaseAuthenticationApi.signOut()
    }

    private fun mapAuthException(e: Exception): Exception {
        return when(e) {
            is FirebaseAuthEmailException -> DuplicateDataException()
            is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
            else -> e
        }
    }
}