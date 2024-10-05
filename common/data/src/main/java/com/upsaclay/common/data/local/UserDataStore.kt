package com.upsaclay.common.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.getFlowGsonValue
import com.upsaclay.common.data.getGsonValue
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.setGsonValue
import kotlinx.coroutines.flow.Flow

internal class UserDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
    private val store = context.dataStore
    private val userKey = stringPreferencesKey("userKey")

    suspend fun storeCurrentUser(user: UserDTO) {
        store.setGsonValue(userKey, user)
    }

    fun getCurrentUserFlow(): Flow<UserDTO?> = store.getFlowGsonValue<UserDTO>(userKey)

    suspend fun getCurrentUser(): UserDTO? = store.getGsonValue<UserDTO>(userKey)
}