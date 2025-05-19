package com.upsaclay.common.data.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

suspend fun <T> DataStore<Preferences>.getValue(key: Preferences.Key<T>): T? = data.map { it[key] }.firstOrNull()

fun <T> DataStore<Preferences>.getFlowValue(key: Preferences.Key<T>): Flow<T?> = data.map { it[key] }

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}

suspend fun <T> DataStore<Preferences>.removeValue(key: Preferences.Key<T>) {
    edit { preferences ->
        preferences.remove(key)
    }
}

suspend fun <T> DataStore<Preferences>.clearAll() {
    edit { preferences ->
        preferences.clear()
    }
}

suspend fun <T> DataStore<Preferences>.contains(key: Preferences.Key<T>): Boolean =
    data.map { preferences ->
        preferences.contains(key)
    }.firstOrNull() ?: false

suspend fun <T> DataStore<Preferences>.setGsonValue(
    key: Preferences.Key<String>,
    value: T,
    gson: Gson = Gson()
) {
    setValue(key, gson.toJson(value))
}

suspend inline fun <reified T> DataStore<Preferences>.getGsonValue(
    key: Preferences.Key<String>,
    gson: Gson = Gson()
): T? {
    val type = object : TypeToken<T>() {}.type
    return getValue(key)?.let {
        runCatching { gson.fromJson<T>(it, type) }.getOrNull()
    }
}

inline fun <reified T> DataStore<Preferences>.getGsonFlowValue(
    key: Preferences.Key<String>,
    gson: Gson = Gson()
): Flow<T?> {
    val type = object : TypeToken<T>() {}.type
    return getFlowValue(key).map {
        runCatching { gson.fromJson<T>(it, type) }.getOrNull()
    }
}