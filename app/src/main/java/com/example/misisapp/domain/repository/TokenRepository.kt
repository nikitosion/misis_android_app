package com.example.misisapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
    suspend fun clearToken()
    fun hasValidToken(): Flow<Boolean>
}