package com.example.misisapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecoverPasswordRequestDto (
    val email: String
)