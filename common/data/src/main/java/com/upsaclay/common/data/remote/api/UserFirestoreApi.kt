package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.remote.FirestoreUser
import kotlinx.coroutines.flow.Flow

internal interface UserFirestoreApi {
    suspend fun getUser(userId: String): FirestoreUser?

    suspend fun getUserFlow(userId: String): Flow<FirestoreUser?>

    suspend fun getUserWithEmail(userEmail: String): FirestoreUser?

    suspend fun getUsers(): List<FirestoreUser>

    suspend fun createUser(firestoreUser: FirestoreUser)

    suspend fun updateProfilePictureFileName(userId: String, fileName: String?)

    suspend fun deleteProfilePictureFileName(userId: String)

    suspend fun isUserExist(email: String): Boolean
}