package com.upsaclay.authentication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.extensions.getFlowValue
import com.upsaclay.common.data.extensions.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class AuthenticationLocalDataSource(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")
    private val store = context.dataStore
    private val authenticationKey = booleanPreferencesKey("authenticationKey")

    suspend fun setAuthenticationState(isAuthenticated: Boolean) = withContext(Dispatchers.IO) {
        store.setValue(authenticationKey, isAuthenticated)
    }

    fun getAuthenticationState(): Flow<Boolean> = store.getFlowValue(authenticationKey).map { it ?: false }
}