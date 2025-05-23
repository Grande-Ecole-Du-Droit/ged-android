package com.upsaclay.common.data.local

import com.upsaclay.common.data.toDTO
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal class UserLocalDataSource(private val userDataStore: UserDataStore) {
    fun getCurrentUserFlow(): Flow<User> = userDataStore.getCurrentUserFlow()
        .filterNotNull().map { it.toUser()}

    suspend fun getCurrentUser(): User? = userDataStore.getCurrentUser()?.toUser()

    suspend fun setCurrentUser(user: User) {
        userDataStore.storeCurrentUser(user.toDTO())
    }

    suspend fun updateProfilePictureFileName(url: String) {
        userDataStore.getCurrentUserFlow().firstOrNull()?.let { userDTO ->
            userDataStore.storeCurrentUser(userDTO.copy(userProfilePictureFileName = url))
        }
    }

    suspend fun deleteProfilePictureFileName() {
        userDataStore.getCurrentUserFlow().firstOrNull()?.let { userDTO ->
            userDataStore.storeCurrentUser(userDTO.copy(userProfilePictureFileName = null))
        }
    }

    suspend fun deleteCurrentUser() {
        userDataStore.removeCurrentUser()
    }
}