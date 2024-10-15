package com.upsaclay.common.data.repository

import com.upsaclay.common.data.UserMapper
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUserFlow: Flow<User> = _currentUser.filterNotNull()
    override val currentUser: User? get() = _currentUser.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                userLocalDataSource.getCurrentUserFlow().collect { userDTO ->
                    _currentUser.value = UserMapper.toDomain(userDTO)
                }
            }
            launch {
                refreshUser()
            }
        }
    }

    override suspend fun getUserWithFirestore(userId: Int): User? =
        userRemoteDataSource.getUserWithFirestore(userId)?.let { UserMapper.toDomain(it) }

    override suspend fun getUserWithFirestore(userEmail: String): User? =
        userRemoteDataSource.getUserWithFirestore(userEmail)?.let { UserMapper.toDomain(it) }

    override suspend fun getUserWithOracle(email: String): User? =
        userRemoteDataSource.getUserWithOracle(email)?.let { UserMapper.toDomain(it) }

    override suspend fun getAllUsers(): List<User> =
        userRemoteDataSource.getAllUsersWithFirestore().map { UserMapper.toDomain(it) }

    override suspend fun getOnlineUsers(): List<User> =
        userRemoteDataSource.getOnlineUsers().map { UserMapper.toDomain(it) }

    override suspend fun createUserWithOracle(user: User): Int? {
        val userDTO = UserMapper.toDTO(user)
        val userId = userRemoteDataSource.createUserWithOracle(userDTO)
        return userId?.also { userLocalDataSource.setUser(userDTO.copy(userId = userId)) }
    }

    override suspend fun createUserWithFirestore(user: User): Result<Unit> {
        if(user.id == -1) return Result.failure(IllegalArgumentException("L'id de l'utilisateur n'a pas été défini"))

        val userFirestoreModel = UserMapper.toFirestoreModel(user)
        return userRemoteDataSource.createUserWithFirestore(userFirestoreModel)
    }

    override suspend fun setCurrentUser(user: User) {
        userLocalDataSource.setUser(UserMapper.toDTO(user))
    }

    override suspend fun removeCurrentUser() {
        userLocalDataSource.removeCurrentUser()
    }

    override suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit> {
        return userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)
            .onSuccess { userLocalDataSource.updateProfilePictureUrl(profilePictureUrl) }
    }

    override suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit> {
        return userRemoteDataSource.deleteProfilePictureUrl(userId)
            .onSuccess { userLocalDataSource.deleteProfilePictureUrl() }
    }

    override suspend fun isUserExist(email: String): Result<Boolean> = userRemoteDataSource.isUserExist(email)

    private suspend fun refreshUser() {
        userLocalDataSource.getCurrentUser()?.let { localUser ->
            userRemoteDataSource.getCurrentUser(localUser.userId!!)?.let { remoteUser ->
                if (localUser != remoteUser) {
                    userLocalDataSource.setUser(remoteUser)
                }
            }
        }
    }
}