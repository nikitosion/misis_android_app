package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.model.DayScheduleItem
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetScheduleForWeekUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val currentUserRepository: CurrentUserRepository
) {
    operator fun invoke(selectedDate: Date): Flow<List<DayScheduleItem>> {

        return flow {
            val currentUser = currentUserRepository.profileCacheFlow().first()
                ?: throw java.lang.IllegalStateException("Пользователь не авторизован")

            val groupId = currentUser.groupId
                ?: throw java.lang.IllegalStateException("В профиле не указана группа")

            val (weekStart, weekEnd) = getWeekRange(selectedDate)

            emitAll(scheduleRepository.getScheduleForWeek(weekStart, weekEnd, currentUser.groupId))
        }
    }
}

private fun getWeekRange(selectedDate: Date): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val diffToMonday = if (dayOfWeek == Calendar.SUNDAY) -6 else Calendar.MONDAY - dayOfWeek
    calendar.add(Calendar.DAY_OF_MONTH, diffToMonday)

    // Resetting time to 00:00
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startOfWeek = calendar.time

    calendar.add(Calendar.DAY_OF_MONTH, 6)
    val endOfWeek = calendar.time

    return Pair(startOfWeek, endOfWeek)
}