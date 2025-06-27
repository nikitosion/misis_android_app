package com.example.misisapp.domain.model.auth

sealed class AuthState {
    object Unknown : AuthState()
    object Authenticated : AuthState()
    object NotAuthenticated : AuthState()
}