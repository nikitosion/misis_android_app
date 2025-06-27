package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.repository.AuthRepository
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return authRepository.recoverPassword(email)
    }
}
