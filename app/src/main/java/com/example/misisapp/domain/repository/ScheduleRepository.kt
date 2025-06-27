package com.example.misisapp.domain.repository

import com.example.misisapp.domain.model.DayScheduleItem
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ScheduleRepository {
    fun getScheduleForWeek(
        weekStart: Date,
        weekEnd: Date,
        groupId: String
    ): Flow<List<DayScheduleItem>>
}