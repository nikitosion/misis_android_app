package com.example.misisapp.data.remote.api

import com.example.misisapp.data.remote.dto.GroupDto
import retrofit2.http.GET

interface GroupApi {
    @GET("groups")
    suspend fun getAllGroups(): List<GroupDto>
}