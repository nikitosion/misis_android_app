package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val currentUserRepository: CurrentUserRepository
) {
    fun getCachedProfile(): Flow<User?> = currentUserRepository.profileCacheFlow()
    suspend fun getUpdateProfile(): Resource<User> = currentUserRepository.getUpdateAndSaveProfile()
}