package com.example.misisapp.domain.repository

import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CurrentUserRepository {
    suspend fun getUpdateAndSaveProfile() : Resource<User>

    fun tokenFlow(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()

    fun profileCacheFlow(): Flow<User?>
    suspend fun saveProfile(user: User)
    suspend fun clearCachedProfile()
}