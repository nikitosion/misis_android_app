package com.example.misisapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrentUserPrefsImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CurrentUserPrefs {

    companion object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
        val PROFILE_JSON = stringPreferencesKey("user_profile_json")
    }

    override fun getTokenFlow(): Flow<String?> = dataStore.data.map { prefs -> prefs[TOKEN] }

    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs -> prefs[TOKEN] = token }
    }

    override suspend fun clearToken() {
        dataStore.edit { prefs -> prefs.remove(TOKEN) }
    }

    override fun getProfileFlow(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[PROFILE_JSON] }

    override suspend fun saveProfileJson(json: String) {
        dataStore.edit { prefs ->
            prefs[PROFILE_JSON] = json
        }
    }

    override suspend fun clearProfileJson() {
        dataStore.edit { prefs -> prefs.remove(PROFILE_JSON) }
    }


}