package com.upsaclay.common.data.remote

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.exceptions.parseOracleException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.api.UserFirestoreApi
import com.upsaclay.common.data.remote.api.UserRetrofitApi
import com.upsaclay.common.data.toLocal
import com.upsaclay.common.data.toFirestoreUser
import com.upsaclay.common.data.toOracleUser
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            createUserWithFirestore(user)
        }
    }

    suspend fun updateProfilePictureFileName(userId: String, fileName: String) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { userRetrofitApi.updateProfilePictureFileName(userId, fileName) }
            )
            userFirestoreApi.updateProfilePictureFileName(userId, fileName)
        }
    }

    suspend fun deleteProfilePictureFileName(userId: String) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { userRetrofitApi.deleteProfilePictureFileName(userId) }
            )
            userFirestoreApi.updateProfilePictureFileName(userId, null)
        }
    }

    suspend fun isUserExist(email: String): Boolean = withContext(Dispatchers.IO) {
        userFirestoreApi.isUserExist(email)
    }

    private suspend fun createUserWithFirestore(user: User) {
        userFirestoreApi.createUser(user.toFirestoreUser())
    }

    private suspend fun createUserWithOracle(user: User) {
        mapServerResponseException(
            block = { userRetrofitApi.createUser(user.toOracleUser()) },
            specificHandle = {
                val errorMessage = formatHttpError(it)
                if (it.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                    throw ForbiddenException(errorMessage)
                }
                throw parseOracleException(it.body()?.code, errorMessage)
            }
        )
    }
}