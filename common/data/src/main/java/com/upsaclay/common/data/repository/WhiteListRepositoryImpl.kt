package com.upsaclay.common.data.repository

import com.upsaclay.common.data.exceptions.mapNetworkException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.api.WhiteListApi
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.common.domain.repository.WhiteListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WhiteListRepositoryImpl(
    private val whiteListApi: WhiteListApi
): WhiteListRepository {
    override suspend fun isUserWhiteListed(email: String): Boolean = withContext(Dispatchers.IO) {
        mapNetworkException(
            message = "Failed to check user white list",
            block = { sendCheckUserWhiteListRequest(email) }
        )
    }

    private suspend fun sendCheckUserWhiteListRequest(email: String): Boolean = withContext(Dispatchers.IO) {
        val response = whiteListApi.isUserWhiteListed(email)
        if (!response.isSuccessful) {
            val errorMessage = formatHttpError(response)
            throw InternalServerException(errorMessage)
        } else {
            response.body() ?: false
        }
    }
}