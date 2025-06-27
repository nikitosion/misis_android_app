package com.example.misisapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto(
    val id: String,
    @SerialName("group_id") val groupId: String,
    @SerialName("teacher_id") val teacherId: String,
    val subject: String,
    val date: String,
    @SerialName("start_time") val startTime: String,
    @SerialName("end_time") val endTime: String,
    val room: String
)