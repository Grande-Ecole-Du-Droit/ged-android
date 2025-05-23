package com.upsaclay.common.data.local

import com.upsaclay.common.domain.entity.FcmToken

class FcmLocalDataSource(
    private val fcmDataStore: FcmDataStore
) {
    suspend fun getUnsentFcmToken(): FcmToken? = fcmDataStore.getFcmToken()

    suspend fun storeUnsentFcmToken(fcmToken: FcmToken) {
        fcmDataStore.storeFcmToken(fcmToken)
    }

    suspend fun removeUnsentFcmToken() {
        fcmDataStore.removeFcmToken()
    }
}