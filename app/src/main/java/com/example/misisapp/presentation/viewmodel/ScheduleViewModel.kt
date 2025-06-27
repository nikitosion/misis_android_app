package com.example.misisapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misisapp.domain.model.DayScheduleItem
import com.example.misisapp.domain.usecase.GetScheduleForWeekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleForWeekUseCase: GetScheduleForWeekUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _schedule = MutableLiveData<List<DayScheduleItem>>()
    val schedule: LiveData<List<DayScheduleItem>> = _schedule

    fun loadSchedule(date: Date) {
        _isLoading.value = true
        viewModelScope.launch {
            getScheduleForWeekUseCase(date)
                .take(1)
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
                .collect { list ->
                    _schedule.value = list
                    _isLoading.value = false
                }
        }
    }

    init {
        loadSchedule(Date())
    }
}