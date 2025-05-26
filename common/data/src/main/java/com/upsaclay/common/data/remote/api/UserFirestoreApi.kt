package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.remote.FirestoreUser
import kotlinx.coroutines.flow.Flow

internal interface UserFirestoreApi {
    suspend fun getUser(userId: String): FirestoreUser?

    fun getUserFlow(userId: String): Flow<FirestoreUser?>

    suspend fun getUserWithEmail(userEmail: String): FirestoreUser?

    suspend fun getUsers(): List<FirestoreUser>

    suspend fun createUser(firestoreUser: FirestoreUser)

    fun updateProfilePictureFileName(userId: String, fileName: String?)

    fun deleteProfilePictureFileName(userId: String)

    suspend fun isUserExist(email: String): Boolean
}