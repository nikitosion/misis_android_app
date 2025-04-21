package com.example.misisapp.ui.schedule

import java.util.Date

data class DayScheduleItem(
    val date: Date,
    val weekday: String,
    val lessons: List<LessonScheduleItem>
)
