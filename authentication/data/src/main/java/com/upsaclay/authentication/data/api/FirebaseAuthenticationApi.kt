package com.upsaclay.authentication.data.api

interface FirebaseAuthenticationApi {
    suspend fun signIn(email: String, password: String)

    suspend fun signUp(email: String, password: String): String

    fun signOut()

    fun isAuthenticated(): Boolean
}