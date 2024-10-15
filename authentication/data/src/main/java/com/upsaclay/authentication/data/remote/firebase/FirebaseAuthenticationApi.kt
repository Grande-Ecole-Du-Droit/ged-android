package com.upsaclay.authentication.data.remote.firebase

interface FirebaseAuthenticationApi {
    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signUpWithEmailAndPassword(email: String, password: String)

    suspend fun sendVerificationEmail()

    fun isUserEmailVerified(): Boolean
}