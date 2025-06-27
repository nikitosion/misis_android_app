package com.example.misisapp.data.remote.api

import com.example.misisapp.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    // Token automatically added by AuthInterceptor

    @GET("auth/me")
    suspend fun getUser(): UserDto

    @GET("auth/users/{fullname}")
    suspend fun getUsersByName(@Path("fullname") fullName: String): List<UserDto>

    @GET("auth/users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto

}