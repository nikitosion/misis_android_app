package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.repository.UserRepository
import com.example.misisapp.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<User>> = userRepository.getUserProfileFlow()
}