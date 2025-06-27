package com.example.misisapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misisapp.domain.usecase.RecoverPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val recoverPasswordUseCase: RecoverPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<RecoverPasswordUiState>()
    val uiState: LiveData<RecoverPasswordUiState> = _uiState

    private val _navigationEvent = MutableLiveData<RecoverPasswordNavigationEvent>()
    val navigationEvent: LiveData<RecoverPasswordNavigationEvent> = _navigationEvent

    init {
        _uiState.value = RecoverPasswordUiState.ShowForm
    }

    fun recoverPassword(email: String) {
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = RecoverPasswordUiState.Error("Please enter a valid email")
            return
        }

        viewModelScope.launch {
            _uiState.value = RecoverPasswordUiState.Loading

            val success = recoverPasswordUseCase(email)
            if (success) {
                _uiState.value = RecoverPasswordUiState.Success("Password recovery email sent")
                delay(2000)
                _navigationEvent.value = RecoverPasswordNavigationEvent.NavigateBack
            } else {
                _uiState.value = RecoverPasswordUiState.Error("Failed to send recovery email")
            }
        }
    }

    fun navigateBack() {
        _navigationEvent.value = RecoverPasswordNavigationEvent.NavigateBack
    }

    fun clearError() {
        if (_uiState.value is RecoverPasswordUiState.Error) {
            _uiState.value = RecoverPasswordUiState.ShowForm
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}