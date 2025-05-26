package com.upsaclay.common.data.remote

import com.upsaclay.common.data.exceptions.parseOracleException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.api.UserFirestoreApi
import com.upsaclay.common.data.remote.api.UserRetrofitApi
import com.upsaclay.common.data.toDTO
import com.upsaclay.common.data.toFirestoreUser
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

internal class UserRemoteDataSource(
    private val userRetrofitApi: UserRetrofitApi,
    private val userFirestoreApi: UserFirestoreApi
) {
    suspend fun getUser(userId: String): User? = withContext(Dispatchers.IO) {
        userFirestoreApi.getUser(userId)?.toUser()
    }

    fun getUserFlow(userId: String): Flow<User?> = userFirestoreApi.getUserFlow(userId).map { it?.toUser() }

    suspend fun getUserFirestoreWithEmail(userEmail: String): User? = withContext(Dispatchers.IO) {
        userFirestoreApi.getUserWithEmail(userEmail)?.toUser()
    }

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        userFirestoreApi.getUsers().map { it.toUser() }
    }

    suspend fun createUser(user: User) {
        withContext(Dispatchers.IO) {
            createUserWithOracle(user)
            userFirestoreApi.createUser(user.toFirestoreUser())
        }
    }

    suspend fun updateProfilePictureFileName(userId: String, fileName: String) {
        withContext(Dispatchers.IO) {
            val response = userRetrofitApi.updateProfilePictureFileName(userId, fileName)
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError(response)
                throw InternalServerException(errorMessage)
            }
            userFirestoreApi.updateProfilePictureFileName(userId, fileName)
        }
    }

    suspend fun deleteProfilePictureFileName(userId: String) {
        withContext(Dispatchers.IO) {
            val response = userRetrofitApi.deleteProfilePictureFileName(userId)
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError(response)
                throw InternalServerException(errorMessage)
            }
            launch { userFirestoreApi.updateProfilePictureFileName(userId, null) }
        }
    }

    suspend fun isUserExist(email: String): Boolean = withContext(Dispatchers.IO) {
        userFirestoreApi.isUserExist(email)
    }

    private suspend fun createUserWithOracle(user: User) {
        val response = userRetrofitApi.createUser(user.toDTO())
        if (!response.isSuccessful) {
            val errorMessage = formatHttpError(response)
            if (response.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                throw ForbiddenException(errorMessage)
            }
            throw parseOracleException(response.body()?.code, errorMessage)
        }
    }
}