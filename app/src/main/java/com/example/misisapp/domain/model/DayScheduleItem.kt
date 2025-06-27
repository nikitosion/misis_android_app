package com.example.misisapp.domain.model

import java.util.Date

data class DayScheduleItem(
    val date: Date,
    val weekday: String,
    val lessons: List<LessonItem>
)