package com.example.misisapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val role: UserRole,
    val groupId: String,
    val fullName: String,
    val email: String,
    val isActive: Boolean
)