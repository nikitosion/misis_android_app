package com.example.misisapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.usecase.GetCurrentUserProfileUseCase
import com.example.misisapp.domain.usecase.LoginUseCase
import com.example.misisapp.domain.utils.Resource.Error
import com.example.misisapp.domain.utils.Resource.Loading
import com.example.misisapp.domain.utils.Resource.Success
import com.example.misisapp.domain.utils.Resource.Unauthorized
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val currentUserRepository: CurrentUserRepository,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            // Trying to login user
            when (val res = loginUseCase(username, password)) {
                is Success -> {
                    // Waiting until token is available
                    currentUserRepository.tokenFlow().filterNotNull().first()

                    // Trying to get user's data
                    val cachedProfileRes = getCurrentUserProfileUseCase.getCachedProfile().first()
                    if (cachedProfileRes != null) {
                        _uiState.value = LoginUiState.Success
                    } else {
                        _uiState.value = LoginUiState.Error("Не удалось загрузить профиль")
                    }
                }
                is Unauthorized -> _uiState.value = LoginUiState.Error("Сессия истекла, войдите снова")
                is Error -> _uiState.value = LoginUiState.Error(res.message)
                is Loading -> {}
            }
        }
    }
}