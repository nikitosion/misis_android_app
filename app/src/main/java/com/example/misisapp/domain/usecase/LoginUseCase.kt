package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.repository.AuthRepository
import com.example.misisapp.domain.utils.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<Unit> =
        authRepository.login(username, password)
}