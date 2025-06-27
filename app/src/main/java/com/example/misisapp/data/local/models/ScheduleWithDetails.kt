package com.example.misisapp.data.local.models

import androidx.room.Embedded
import com.example.misisapp.data.local.entity.ScheduleEntity

data class ScheduleWithDetails(
    @Embedded val schedule: ScheduleEntity,
    val teacherName: String?,
    val groupName: String?
)