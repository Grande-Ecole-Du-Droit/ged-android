package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.FcmToken

interface CredentialsRepository {
    suspend fun getUnsentFcmToken(): FcmToken?

    suspend fun sendFcmToken(token: FcmToken)

    suspend fun storeUnsentFcmToken(token: FcmToken)

    suspend fun removeUnsentFcmToken()
}