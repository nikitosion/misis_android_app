package com.example.misisapp.data.mapper

import com.example.misisapp.data.local.entity.ScheduleEntity
import com.example.misisapp.data.local.models.ScheduleWithDetails
import com.example.misisapp.data.remote.dto.ScheduleDto
import com.example.misisapp.domain.model.DayScheduleItem
import com.example.misisapp.domain.model.LessonItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleMapper @Inject constructor() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun mapToEntities(dtos: List<ScheduleDto>): List<ScheduleEntity> {
        return dtos.map { dto ->
            ScheduleEntity(
                id = dto.id,
                groupID = dto.groupId,
                teacherID = dto.teacherId,
                subject = dto.subject,
                date = dateFormat.parse(dto.date) ?: Date(),
                startTime = timeFormat.parse(dto.startTime) ?: Date(),
                endTime = timeFormat.parse(dto.endTime) ?: Date(),
                room = dto.room
            )
        }
    }

    fun mapToLessonItem(details: ScheduleWithDetails): LessonItem {
        return LessonItem(
            date = details.schedule.date,
            subject = details.schedule.subject,
            teacher = details.teacherName ?: "",
            groups = listOfNotNull(details.groupName),
            lessonType = "",
            auditorium = details.schedule.room,
            address = "",
            link = "",
            comment = "",
            id = details.schedule.id,
            startTime = details.schedule.startTime,
            endTime = details.schedule.endTime
        )
    }

    fun mapToDayScheduleItems(detailsList: List<ScheduleWithDetails>): List<DayScheduleItem> {
        val grouped = detailsList.groupBy {
            clearTime(it.schedule.date)
        }
        return grouped.map { (date, lessonsForDay) ->
            DayScheduleItem(
                date = date,
                weekday = getWeekdayName(date),
                lessons = lessonsForDay.map { mapToLessonItem(it) }
            )
        }.sortedBy { it.date }
    }

    private fun clearTime(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun getWeekdayName(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Понедельник"
            Calendar.TUESDAY -> "Вторник"
            Calendar.WEDNESDAY -> "Среда"
            Calendar.THURSDAY -> "Четверг"
            Calendar.FRIDAY -> "Пятница"
            Calendar.SATURDAY -> "Суббота"
            Calendar.SUNDAY -> "Воскресенье"
            else -> ""
        }
    }
}