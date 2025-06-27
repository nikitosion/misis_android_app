package com.example.misisapp.data.repository

import android.util.Log
import com.example.misisapp.data.local.dao.ScheduleDao
import com.example.misisapp.data.local.dao.UserDao
import com.example.misisapp.data.mapper.ScheduleMapper
import com.example.misisapp.data.mapper.UserMapper
import com.example.misisapp.data.remote.api.ScheduleApi
import com.example.misisapp.data.remote.api.UserApi
import com.example.misisapp.domain.model.DayScheduleItem
import com.example.misisapp.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleApi: ScheduleApi,
    private val scheduleDao: ScheduleDao,
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val scheduleMapper: ScheduleMapper,
    private val userMapper: UserMapper
) :
    ScheduleRepository {

    override fun getScheduleForWeek(
        weekStart: Date,
        weekEnd: Date,
        groupdId: String
    ): Flow<List<DayScheduleItem>> {
        return scheduleDao.getScheduleForWeekFlow(weekStart, weekEnd)
            .map { scheduleMapper.mapToDayScheduleItems(it) }
            .onStart {
                try {
                    refreshScheduleForWeek(weekStart, weekEnd, groupdId)
                } catch (e: Exception) {
                    // TODO: Обработать ошибки сети
                }
            }
    }

    private suspend fun refreshScheduleForWeek(weekStart: Date, weekEnd: Date, groupdId: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        Log.i("ScheduleRepostitoryImplTAG", "${formatter.format(weekStart)} | ${formatter.format(weekEnd)} | $groupdId")

        val remoteDtos = scheduleApi.getScheduleForWeek(
            formatter.format(weekStart),
            formatter.format(weekEnd),
            groupdId
        )

        val entities = scheduleMapper.mapToEntities(remoteDtos)
        scheduleDao.deleteScheduleForWeek(weekStart, weekEnd)
        scheduleDao.insertSchedules(entities)

        val teacherIds = remoteDtos.map { it.teacherId }.distinct()
        teacherIds.forEach { teacherId ->
            try {
                val userDto = userApi.getUserById(teacherId)
                userDao.insertUser(userMapper.mapToEntity(userDto))
            } catch (e: Exception) {
            }
        }
    }

}