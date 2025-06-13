package com.upsaclay.common.data.local

import com.upsaclay.common.data.toLocal
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class UserLocalDataSource(private val userDataStore: UserDataStore) {
    fun getUserFlow(): Flow<User> = userDataStore.getUserFlow().filterNotNull().map { it.toUser()}

    suspend fun getUser(): User? = withContext(Dispatchers.IO) {
        userDataStore.getUser()?.toUser()
    }

    suspend fun storeUser(user: User) {
        withContext(Dispatchers.IO) {
            userDataStore.storeUser(user.toLocal())
        }
    }

    suspend fun updateProfilePictureFileName(fileName: String?) {
        withContext(Dispatchers.IO) {
            userDataStore.getUser()?.let { userDTO ->
                userDataStore.storeUser(userDTO.copy(userProfilePictureFileName = fileName))
            }
        }
    }

    suspend fun removeUser() {
        withContext(Dispatchers.IO) {
            userDataStore.removeCurrentUser()
        }
    }
}