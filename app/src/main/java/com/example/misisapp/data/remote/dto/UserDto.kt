package com.example.misisapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val role: String,
    @SerialName("group_id") val groudId: String,
    @SerialName("full_name") val fullName: String,
    val email: String,
    @SerialName("is_active") val isActive: Boolean,
)