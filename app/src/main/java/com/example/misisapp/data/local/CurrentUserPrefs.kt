package com.example.misisapp.data.local

import kotlinx.coroutines.flow.Flow

interface CurrentUserPrefs {
    fun getTokenFlow(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()

    fun getProfileFlow(): Flow<String?>
    suspend fun saveProfileJson(json: String)
    suspend fun clearProfileJson()
}