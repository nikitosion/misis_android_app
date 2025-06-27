package com.example.misisapp.data.remote.api

import com.example.misisapp.data.remote.dto.TokenResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    @FormUrlEncoded
    @POST("auth/token")
    suspend fun login(@Field("username") username: String, @Field("password") password: String): TokenResponseDto

    /*@POST("auth/recover_password")
    suspend fun recoverPassword()*/


}