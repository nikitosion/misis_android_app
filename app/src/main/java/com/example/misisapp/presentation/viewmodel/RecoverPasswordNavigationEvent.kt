package com.example.misisapp.presentation.viewmodel

sealed class RecoverPasswordNavigationEvent {
    object NavigateBack : RecoverPasswordNavigationEvent()
}