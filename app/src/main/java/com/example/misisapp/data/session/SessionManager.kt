package com.example.misisapp.data.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _unauthorized = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val unauthorized: SharedFlow<Unit> = _unauthorized

    suspend fun notifyUnauthorized() {
        _unauthorized.emit(Unit)
    }
}