package com.example.misisapp.data.remote.interceptor

import com.example.misisapp.data.local.CurrentUserPrefs
import com.example.misisapp.data.session.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val prefs: CurrentUserPrefs,
    private val sessionManager: SessionManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401 || response.code == 403) {
            runBlocking {
                prefs.clearToken()
                prefs.clearProfileJson()
                sessionManager.notifyUnauthorized()
            }
        }
        return null
    }
}