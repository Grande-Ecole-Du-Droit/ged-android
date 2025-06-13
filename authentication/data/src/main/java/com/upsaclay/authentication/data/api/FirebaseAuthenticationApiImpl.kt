package com.upsaclay.authentication.data.api

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthenticationApiImpl: FirebaseAuthenticationApi {
    private val firebaseAuth = Firebase.auth

    override suspend fun signIn(email: String, password: String) {
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { if (continuation.isActive) continuation.resume(Unit) }
                .addOnFailureListener { if (continuation.isActive) continuation.resumeWithException(it) }
        }
    }

    override suspend fun signUp(email: String, password: String): String = suspendCancellableCoroutine { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { continuation.resume(it.user!!.uid) }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun isAuthenticated(): Boolean = firebaseAuth.currentUser != null
}