package com.example.misisapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val TOKEN_TIMESTAMP = longPreferencesKey("token_timestamp")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefences ->
            prefences[PreferencesKeys.AUTH_TOKEN] = token
            prefences[PreferencesKeys.TOKEN_TIMESTAMP] = System.currentTimeMillis()
        }
    }

    suspend fun getToken() : String? {
        return context.dataStore.data.first()[PreferencesKeys.AUTH_TOKEN]
    }

    fun getTokenSync(): String? {
        return runBlocking {
            try {
                context.dataStore.data.first()[PreferencesKeys.AUTH_TOKEN]
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AUTH_TOKEN)
            preferences.remove(PreferencesKeys.TOKEN_TIMESTAMP)
        }
    }

    fun hasValidToken(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            val token = preferences[PreferencesKeys.AUTH_TOKEN]
            val timestamp = preferences[PreferencesKeys.TOKEN_TIMESTAMP] ?: 0L
            /*val isExpired = System.currentTimeMillis() - timestamp > TOKEN_EXPIRY_TIME*/
            !token.isNullOrEmpty() /*&& !isExpired*/
        }
    }

    /*companion object {
        private const val TOKEN_EXPIRY_TIME = 24 * 60 * 60 * 1000L
    }*/
}