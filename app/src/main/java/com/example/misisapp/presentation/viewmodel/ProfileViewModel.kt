package com.example.misisapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.usecase.GetCurrentUserProfileUseCase
import com.example.misisapp.domain.utils.Resource
import com.example.misisapp.presentation.ui.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val currentUserRepository: CurrentUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = when (val profile = getCurrentUserProfileUseCase.getUpdateProfile()) {
                is Resource.Loading -> ProfileState.Loading
                is Resource.Success -> {
                        ProfileState.Success(profile.data)
                }
                is Resource.Unauthorized -> {
                    ProfileState.Error("Неавторизован")
                }
                is Resource.Error -> {
                    getCachedProfile()
                    ProfileState.Error(profile.message)

                }
            }
        }

    }

    init {
        loadProfile()
    }

    fun getCachedProfile(): LiveData<User?> =
        currentUserRepository.profileCacheFlow().asLiveData()
}