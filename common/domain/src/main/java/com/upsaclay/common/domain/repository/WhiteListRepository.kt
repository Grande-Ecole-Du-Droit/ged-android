package com.upsaclay.common.domain.repository

interface WhiteListRepository {
    suspend fun isUserWhiteListed(email: String): Boolean
}