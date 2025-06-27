package com.example.misisapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.misisapp.data.local.entity.ScheduleEntity
import com.example.misisapp.data.local.models.ScheduleWithDetails
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ScheduleDao {
    @Query("""
        SELECT s.*, u.fullName as teacherName, g.name as groupName
        FROM schedules s
        LEFT JOIN users u ON s.teacherId = u.id
        LEFT JOIN 'groups' g ON s.groupId = g.id
        WHERE s.date >= :weekStart AND s.date <= :weekEnd
        ORDER BY s.date, s.startTime
    """)
    fun getScheduleForWeekFlow(weekStart: Date, weekEnd: Date) : Flow<List<ScheduleWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)

    @Query("DELETE FROM schedules WHERE date >= :weekStart AND date <= :weekEnd")
    suspend fun deleteScheduleForWeek(weekStart: Date, weekEnd: Date)
}

