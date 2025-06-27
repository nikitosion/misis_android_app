package com.example.misisapp.presentation.viewmodel

sealed class LoginUiState {
    object Idle    : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}