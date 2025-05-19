package com.upsaclay.authentication.data.api

interface FirebaseAuthenticationApi {
    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signUpWithEmailAndPassword(email: String, password: String)

    suspend fun signOut()

    fun isAuthenticated(): Boolean
}