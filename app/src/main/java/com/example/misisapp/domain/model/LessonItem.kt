package com.example.misisapp.domain.model

import java.util.Date

data class LessonItem(
    val id: String,
    val date: Date,
    val subject: String,
    val teacher: String,
    val groups: List<String>,
    val lessonType: String,
    val auditorium: String,
    val address: String,
    val link: String,
    val comment: String,
    val startTime: Date,
    val endTime: Date
)