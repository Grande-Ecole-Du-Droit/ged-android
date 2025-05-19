package com.upsaclay.common.data.repository

import com.upsaclay.common.data.exceptions.handleNetworkException
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    scope: CoroutineScope
) : UserRepository {
    private val _user = userLocalDataSource.getCurrentUserFlow()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    override val user: Flow<User?> = _user
    override val currentUser: User?
        get() = _user.value

    override suspend fun getUsers(): List<User> {
        return handleNetworkException(
            message = "Failed to get users",
            block = { userRemoteDataSource.getUsers() },
        )
    }

    override suspend fun getUser(userId: String): User? {
        return handleNetworkException(
            message = "Failed to get user",
            block = { userRemoteDataSource.getUser(userId) },
        )
    }

    override suspend fun getCurrentUser(): User? = userLocalDataSource.getCurrentUser()

    override suspend fun getUserFlow(userId: String): Flow<User> {
        return handleNetworkException(
            message = "Failed to listen remote user",
            block = { userRemoteDataSource.getUserFlow(userId) }
        )
    }

    override suspend fun getUserWithEmail(userEmail: String): User? {
        return handleNetworkException(
            message = "Failed to get user with email",
            block = { userRemoteDataSource.getUserFirestoreWithEmail(userEmail) }
        )
    }

    override suspend fun createUser(user: User) {
        handleNetworkException(
            message = "Failed to create user",
            block = {
                userRemoteDataSource.createUser(user)
                userLocalDataSource.setCurrentUser(user)
            }
        )
    }

    override suspend fun setCurrentUser(user: User) {
        userLocalDataSource.setCurrentUser(user)
    }

    override suspend fun deleteCurrentUser() {
        userLocalDataSource.deleteCurrentUser()
    }

    override suspend fun updateProfilePictureFileName(userId: String, fileName: String) {
        handleNetworkException(
            message = "Failed to update profile picture file name",
            block = {
                userRemoteDataSource.updateProfilePictureFileName(userId, fileName)
                userLocalDataSource.updateProfilePictureFileName(fileName)
            }
        )
    }

    override suspend fun deleteProfilePictureUrl(userId: String) {
        handleNetworkException(
            block = {
                userRemoteDataSource.deleteProfilePictureFileName(userId)
                userLocalDataSource.deleteProfilePictureFileName()
            }
        )
    }

    override suspend fun isUserExist(email: String): Boolean {
        return handleNetworkException(
            message = "Failed to check if user exists",
            block = { userRemoteDataSource.isUserExist(email) }
        )
    }
}