package com.upsaclay.authentication.data.repository.firebase

import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.upsaclay.authentication.data.api.FirebaseAuthenticationApi
import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.authentication.domain.entity.exception.UserDisabledException
import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.domain.entity.DuplicateDataException
import com.upsaclay.common.domain.entity.ForbiddenException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseAuthenticationRepositoryImpl(
    private val firebaseAuthenticationApi: FirebaseAuthenticationApi
): FirebaseAuthenticationRepository {
    override fun isAuthenticated(): Boolean = firebaseAuthenticationApi.isAuthenticated()

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        withContext(Dispatchers.IO) {
            mapFirebaseException(
                message = "Failed to login with email and password",
                block = { firebaseAuthenticationApi.signIn(email, password) },
                specificMap = ::mapAuthException
            )
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            mapFirebaseException(
                message = "Failed to register with email and password",
                block = { firebaseAuthenticationApi.signUp(email, password) },
                specificMap = ::mapAuthException
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
            is FirebaseAuthInvalidUserException -> mapAuthErrorCode(e)
            is FirebaseAuthUserCollisionException -> DuplicateDataException()
            is FirebaseAuthException -> mapAuthErrorCode(e)
            else -> e
        }
    }

    private fun mapAuthErrorCode(e: FirebaseAuthException): Exception {
        return when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> DuplicateDataException()
            "ERROR_INVALID_EMAIL" -> InvalidCredentialsException()
            "ERROR_USER_NOT_FOUND" -> InvalidCredentialsException()
            "ERROR_USER_DISABLED" -> UserDisabledException()
            "ERROR_OPERATION_NOT_ALLOWED" -> ForbiddenException()
            else -> e
        }
    }
}