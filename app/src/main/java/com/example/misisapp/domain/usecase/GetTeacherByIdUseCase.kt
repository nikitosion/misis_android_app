package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.repository.UserRepository
import javax.inject.Inject

class GetTeacherByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(teacherId: String): User? {
        return userRepository.getUserByID(teacherId)
    }
}