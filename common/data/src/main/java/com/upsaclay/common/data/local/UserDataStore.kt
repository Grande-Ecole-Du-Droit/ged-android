package com.upsaclay.common.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.extensions.getGsonFlowValue
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.setGsonValue
import kotlinx.coroutines.flow.Flow

internal class UserDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
    private val store = context.dataStore
    private val userKey = stringPreferencesKey("userKey")

    suspend fun storeUser(user: LocalUser?) {
        store.setGsonValue(userKey, user)
    }

    fun getUserFlow(): Flow<LocalUser?> = store.getGsonFlowValue<LocalUser>(userKey)

    suspend fun getUser(): LocalUser? = store.getGsonValue(userKey)

    suspend fun removeCurrentUser() {
        store.edit { it.remove(userKey) }
    }
}