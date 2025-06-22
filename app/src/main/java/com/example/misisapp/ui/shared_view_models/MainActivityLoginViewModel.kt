package com.example.misisapp.ui.shared_view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar
import java.util.Date

class MainActivityLoginViewModel : ViewModel() {
    private val _selectedWeek = MutableLiveData<Pair<Date, Date>>()
    val selectedWeek: LiveData<Pair<Date, Date>> = _selectedWeek

    fun setSelectedWeekByNumbers(day: Int, month: Int, year: Int) {
        _selectedWeek.value = transformToStartEndWeek(day, month, year)
    }

    private fun transformToStartEndWeek(day: Int, month: Int, year: Int) : Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day) // В Calendar месяцы начинаются с 0

        // Определяем день недели (в Calendar: воскресенье=1, понедельник=2, ..., суббота=7)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        // Сдвиг до понедельника
        val diffToMonday = if (dayOfWeek == Calendar.SUNDAY) -6 else Calendar.MONDAY - dayOfWeek
        calendar.add(Calendar.DAY_OF_MONTH, diffToMonday)
        val startOfWeek = calendar.time

        // Переходим к воскресенью этой недели
        calendar.add(Calendar.DAY_OF_MONTH, 6)
        val endOfWeek = calendar.time

        return Pair(startOfWeek, endOfWeek)
    }
}