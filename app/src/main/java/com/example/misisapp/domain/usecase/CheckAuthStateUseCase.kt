package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.model.auth.AuthState
import com.example.misisapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun invoke(): Flow<AuthState> = authRepository.getAuthState()
}