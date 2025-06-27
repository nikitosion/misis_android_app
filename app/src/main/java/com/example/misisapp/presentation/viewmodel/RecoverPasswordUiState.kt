package com.example.misisapp.presentation.viewmodel

sealed class RecoverPasswordUiState {
    object Loading : RecoverPasswordUiState()
    object ShowForm : RecoverPasswordUiState()
    data class Success(val message: String) : RecoverPasswordUiState()
    data class Error(val message: String) : RecoverPasswordUiState()
}