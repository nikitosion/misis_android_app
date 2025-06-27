package com.example.misisapp.domain.repository

import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserByID(id: String): User?
    suspend fun getCurrentUserProfile(): User?
    fun getUserProfileFlow(): Flow<Resource<User>>
    fun getAllUsersFlow(): Flow<List<User>>
    suspend fun getCurrentUserId(): String?
}