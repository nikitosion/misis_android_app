package com.example.misisapp.data.remote.api

import com.example.misisapp.data.remote.dto.ScheduleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {
    @GET("schedule")
    suspend fun getScheduleForWeek(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("group_id") groupId: String
    ): List<ScheduleDto>
}