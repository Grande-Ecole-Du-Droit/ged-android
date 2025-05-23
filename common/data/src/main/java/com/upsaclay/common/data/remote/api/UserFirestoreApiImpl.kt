package com.upsaclay.common.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.upsaclay.common.data.UserField
import com.upsaclay.common.data.remote.FirestoreUser
import com.upsaclay.common.domain.e
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
private const val TABLE_NAME = "users"

internal class UserFirestoreApiImpl : UserFirestoreApi {
    private val usersCollection = Firebase.firestore.collection(TABLE_NAME)

    override suspend fun getUser(userId: String): FirestoreUser? = suspendCoroutine { continuation ->
        usersCollection.document(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(FirestoreUser::class.java)
                continuation.resume(user)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override fun getUserFlow(userId: String): Flow<FirestoreUser?> = callbackFlow {
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    e("Error getting firestore user", it)
                    trySend(null)
                }

                snapshot?.let {
                    val user = it.toObject(FirestoreUser::class.java)
                    trySend(user)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getUserWithEmail(userEmail: String): FirestoreUser? = suspendCoroutine { continuation ->
        usersCollection.whereEqualTo(UserField.EMAIL, userEmail)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.documents.firstOrNull()?.toObject(FirestoreUser::class.java)
                continuation.resume(user)
            }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override suspend fun getUsers(): List<FirestoreUser> = suspendCoroutine { continuation ->
        usersCollection
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot?.documents?.mapNotNull {
                    it.toObject(FirestoreUser::class.java)
                } ?: emptyList()

                continuation.resume(users)
            }
            .addOnFailureListener { continuation.resume(emptyList()) }
    }

    override suspend fun createUser(firestoreUser: FirestoreUser) {
        suspendCoroutine { continuation ->
            usersCollection.document(firestoreUser.userId).set(firestoreUser)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun updateProfilePictureFileName(userId: String, fileName: String?) {
        suspendCoroutine { continuation ->
            usersCollection.document(userId)
                .update(UserField.PROFILE_PICTURE_FILE_NAME, fileName)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun deleteProfilePictureFileName(userId: String) {
        suspendCoroutine { continuation ->
            usersCollection.document(userId)
                .update(UserField.PROFILE_PICTURE_FILE_NAME, FieldValue.delete())
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun isUserExist(email: String): Boolean = suspendCoroutine { continuation ->
        usersCollection.whereEqualTo(UserField.EMAIL, email).get()
            .addOnSuccessListener { continuation.resume(!it.isEmpty) }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }
}