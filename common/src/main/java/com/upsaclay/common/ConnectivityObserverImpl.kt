package com.upsaclay.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import androidx.core.content.getSystemService
import com.upsaclay.common.domain.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

@SuppressLint("MissingPermission")
class ConnectivityObserverImpl(context: Context, scope: CoroutineScope): ConnectivityObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    private val _isConnected = callbackFlow {
        val callback = object : NetworkCallback() {
            override fun onUnavailable() { trySend(false) }
            override fun onAvailable(network: Network) { trySend(true) }
            override fun onLost(network: Network) { trySend(false) }
        }

        connectivityManager?.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager?.unregisterNetworkCallback(callback) }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )
    override val connected: Flow<Boolean> = _isConnected
    override val isConnected: Boolean
        get() = _isConnected.value
}