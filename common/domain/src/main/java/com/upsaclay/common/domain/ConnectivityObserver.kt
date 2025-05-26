package com.upsaclay.common.domain

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val connected: Flow<Boolean>
    val isConnected: Boolean
}