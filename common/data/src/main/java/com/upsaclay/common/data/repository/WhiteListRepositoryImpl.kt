package com.upsaclay.common.data.repository

import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.api.WhiteListApi
import com.upsaclay.common.domain.repository.WhiteListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WhiteListRepositoryImpl(
    private val whiteListApi: WhiteListApi
): WhiteListRepository {
    override suspend fun isUserWhiteListed(email: String): Boolean = withContext(Dispatchers.IO) {
        mapServerResponseException(
            block = { whiteListApi.isUserWhiteListed(email) }
        ) == true
    }
}