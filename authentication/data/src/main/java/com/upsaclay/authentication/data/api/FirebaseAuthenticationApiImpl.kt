package com.upsaclay.authentication.data.api

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthenticationApiImpl: FirebaseAuthenticationApi {
    private val firebaseAuth = Firebase.auth

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { if (continuation.isActive) continuation.resume(Unit) }
                .addOnFailureListener { if (continuation.isActive) continuation.resumeWithException(it) }
        }
    }

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { if (continuation.isActive) continuation.resume(Unit) }
                .addOnFailureListener { if (continuation.isActive) continuation.resumeWithException(it) }
        }
    }

    override suspend fun signOut() {
        suspendCoroutine { continuation ->
            firebaseAuth.signOut()
            continuation.resume(Unit)
        }
    }

    override fun isAuthenticated(): Boolean = firebaseAuth.currentUser != null
}