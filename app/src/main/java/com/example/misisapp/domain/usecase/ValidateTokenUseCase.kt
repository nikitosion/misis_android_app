package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.repository.AuthRepository
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.validateToken()
    }
}