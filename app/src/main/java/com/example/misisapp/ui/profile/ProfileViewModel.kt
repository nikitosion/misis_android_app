package com.example.misisapp.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.misisapp.ui.login.data.LoginDataSource
import com.example.misisapp.ui.login.data.Result
import com.example.misisapp.ui.login.data.model.LoggedInUser
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val loginDataSource = LoginDataSource(application.baseContext)

    private val _user = MutableLiveData<LoggedInUser>()
    val user: LiveData<LoggedInUser> = _user

    private val _getInfoResult = MutableLiveData<Result<LoggedInUser>>()
    val getInfoResult: LiveData<Result<LoggedInUser>> = _getInfoResult

    fun loadProfile() {
        viewModelScope.launch {
            _getInfoResult.value = loginDataSource.loadUserFromToken()

            if (_getInfoResult.value is Result.Success) {
                _user.value = (_getInfoResult.value as Result.Success<LoggedInUser>).data
            } else {
                Log.e("ProfileViewModel", "Ошибка при загрузке профиля")
            }
        }
    }

    fun clearSavedUserToken() {
        loginDataSource.logout()
    }
}