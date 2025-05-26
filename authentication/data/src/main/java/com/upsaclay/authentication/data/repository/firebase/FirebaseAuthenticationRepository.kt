package com.upsaclay.authentication.data.repository.firebase

interface FirebaseAuthenticationRepository {
    fun isAuthenticated(): Boolean

    suspend fun loginWithEmailAndPassword(email: String, password: String)

    suspend fun registerWithEmailAndPassword(email: String, password: String): String

    fun logout()
}