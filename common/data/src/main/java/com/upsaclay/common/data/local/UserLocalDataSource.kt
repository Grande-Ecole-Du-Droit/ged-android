package com.upsaclay.common.data.local

import com.upsaclay.common.data.toDTO
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class UserLocalDataSource(private val userDataStore: UserDataStore) {
    fun getUserFlow(): Flow<User> = userDataStore.getUserFlow()
        .filterNotNull().map { it.toUser()}

    suspend fun getUser(): User? = userDataStore.getUser()?.toUser()

    suspend fun storeUser(user: User?) {
        userDataStore.storeUser(user?.toDTO())
    }

    suspend fun updateProfilePictureFileName(url: String) {
        userDataStore.getUser()?.let { userDTO ->
            userDataStore.storeUser(userDTO.copy(userProfilePictureFileName = url))
        }
    }

    suspend fun deleteProfilePictureFileName() {
        userDataStore.getUser()?.let { userDTO ->
            userDataStore.storeUser(userDTO.copy(userProfilePictureFileName = null))
        }
    }

    suspend fun removeUser() {
        userDataStore.removeCurrentUser()
    }
}