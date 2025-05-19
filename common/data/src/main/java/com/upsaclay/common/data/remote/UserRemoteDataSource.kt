package com.upsaclay.common.data.remote

import com.upsaclay.common.data.UserMapper
import com.upsaclay.common.data.exceptions.parseOracleException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.api.UserFirestoreApi
import com.upsaclay.common.data.remote.api.UserRetrofitApi
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

internal class UserRemoteDataSource(
    private val userRetrofitApi: UserRetrofitApi,
    private val userFirestoreApi: UserFirestoreApi
) {
    suspend fun getUser(userId: String): User? = withContext(Dispatchers.IO) {
        userFirestoreApi.getUser(userId)
            ?.let(UserMapper::toDomain)
    }

    suspend fun getUserFlow(userId: String): Flow<User> = withContext(Dispatchers.IO) {
        userFirestoreApi.getUserFlow(userId)
            .mapNotNull { it?.let(UserMapper::toDomain) }
    }

    suspend fun getUserFirestoreWithEmail(userEmail: String): User? = withContext(Dispatchers.IO) {
        userFirestoreApi.getUserWithEmail(userEmail)
            ?.let(UserMapper::toDomain)
    }

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        userFirestoreApi.getUsers().map(UserMapper::toDomain)
    }

    suspend fun createUser(user: User) {
        withContext(Dispatchers.IO) {
            createUserWithOracle(user)
            createUserWithFirestore(user)
        }
    }

    suspend fun updateProfilePictureFileName(userId: String, fileName: String) {
        withContext(Dispatchers.IO) {
            launch { userFirestoreApi.updateProfilePictureFileName(userId, fileName) }
            launch {
                val response = userRetrofitApi.updateProfilePictureFileName(userId, fileName)
                if (!response.isSuccessful) {
                    val errorMessage = formatHttpError("Error updating profile picture file name", response)
                    throw InternalServerException(errorMessage)
                }
            }.join()
        }
    }

    suspend fun deleteProfilePictureFileName(userId: String) {
        withContext(Dispatchers.IO) {
            val response = userRetrofitApi.deleteProfilePictureFileName(userId)
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError("Error deleting profile picture file name", response)
                throw InternalServerException(errorMessage)
            }
            launch { userFirestoreApi.updateProfilePictureFileName(userId, null) }
        }
    }

    suspend fun isUserExist(email: String): Boolean = withContext(Dispatchers.IO) {
        userFirestoreApi.isUserExist(email)
    }

    private suspend fun createUserWithOracle(user: User) {
        val response = userRetrofitApi.createUser(UserMapper.toDTO(user))
        if (!response.isSuccessful) {
            val errorMessage = formatHttpError("Error creating user with Oracle", response)
            if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                throw ForbiddenException(errorMessage)
            }
            throw parseOracleException(response.body()?.code, errorMessage)
        }
    }

    private suspend fun createUserWithFirestore(user: User) {
        userFirestoreApi.createUser(UserMapper.toFirestoreUser(user))
    }
}