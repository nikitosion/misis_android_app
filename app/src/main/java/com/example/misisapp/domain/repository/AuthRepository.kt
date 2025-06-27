package com.example.misisapp.domain.repository

import com.example.misisapp.domain.model.auth.AuthState
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Resource<Unit>
    suspend fun recoverPassword(email: String): Boolean
    suspend fun logout()
    suspend fun getAuthToken(): String?
    suspend fun saveAuthToken(token: String)
    suspend fun clearAuthToken()
    fun getAuthState(): Flow<AuthState>
    suspend fun validateToken(): Boolean
}