package com.example.misisapp.ui.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userID: String,
    val fullName: String = "",
    val email: String = "",
    val role: String = "",
    val groupName: String = "",
    val isActive: Boolean = true,
    val displayName: String
)