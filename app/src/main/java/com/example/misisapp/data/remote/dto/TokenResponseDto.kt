package com.example.misisapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto (
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String
)