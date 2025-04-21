package com.example.misisapp.ui.schedule

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class LessonScheduleItem(
    val date: Date,
    val subject: String,
    val teacher: String,
    val groups: List<String>,
    val lessonType: String,
    val auditorium: String,
    val address: String,
    val link: String,
    val comment: String,
) : Parcelable