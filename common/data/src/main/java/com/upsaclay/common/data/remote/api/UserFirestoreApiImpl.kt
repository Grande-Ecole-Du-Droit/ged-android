package com.upsaclay.common.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.upsaclay.common.data.UserField.Firestore.EMAIL
import com.upsaclay.common.data.UserField.Firestore.PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.remote.FirestoreUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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
                    close(it)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val user = it.toObject(FirestoreUser::class.java)
                    trySend(user)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getUserWithEmail(userEmail: String): FirestoreUser? =
        usersCollection.whereEqualTo(EMAIL, userEmail)
            .get()
            .await()
            .firstOrNull()
            .let {
                it?.toObject(FirestoreUser::class.java)
            }

    override suspend fun getUsers(): List<FirestoreUser> =
        usersCollection
            .limit(20)
            .get()
            .await()
            .mapNotNull {
                it.toObject(FirestoreUser::class.java)
            }

    override suspend fun createUser(firestoreUser: FirestoreUser) {
        usersCollection.document(firestoreUser.userId)
            .set(firestoreUser)
            .await()
    }

    override suspend fun updateProfilePictureFileName(userId: String, fileName: String?) {
        usersCollection.document(userId)
            .update(PROFILE_PICTURE_FILE_NAME, fileName)
            .await()
    }

    override suspend fun deleteProfilePictureFileName(userId: String) {
        usersCollection.document(userId)
            .update(PROFILE_PICTURE_FILE_NAME, FieldValue.delete())
            .await()
    }

    override suspend fun isUserExist(email: String): Boolean =
        usersCollection.whereEqualTo(EMAIL, email)
            .get()
            .await()
            .isEmpty.not()
}