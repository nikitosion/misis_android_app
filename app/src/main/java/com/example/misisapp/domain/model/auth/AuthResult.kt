package com.example.misisapp.domain.model.auth

import com.example.misisapp.domain.model.User

sealed class AuthResult {
    data class Success(val token: String, val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}