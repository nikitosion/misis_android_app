package com.example.misisapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "schedules")
data class ScheduleEntity (
    @PrimaryKey val id: String,
    val groupID: String,
    val teacherID: String,
    val subject: String,
    val date: Date,
    val startTime: Date,
    val endTime: Date,
    val room: String
)

