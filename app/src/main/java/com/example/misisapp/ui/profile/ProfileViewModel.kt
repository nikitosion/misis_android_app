package com.example.misisapp.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.misisapp.ui.data.LoginDataSource
import com.example.misisapp.ui.data.Result
import com.example.misisapp.ui.data.model.LoggedInUser
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val loginDataSource = LoginDataSource(context)

    private val _user = MutableLiveData<LoggedInUser>()
    val user: LiveData<LoggedInUser> = _user

    fun loadProfile() {
        viewModelScope.launch {
            when (val result = loginDataSource.loadUserFromToken()) {
                is Result.Success -> _user.value = result.data
                is Result.Error -> Log.e("ProfileViewModel", "Ошибка: ${result.exception}")
            }
        }
    }
}