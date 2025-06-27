package com.example.misisapp.presentation.ui.profile

import com.example.misisapp.domain.model.User

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}