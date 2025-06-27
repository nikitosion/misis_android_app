package com.example.misisapp.data.remote.interceptor

import com.example.misisapp.data.local.CurrentUserPrefs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val prefs: CurrentUserPrefs
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (isAuthRequest(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            prefs.getTokenFlow().first()
        }

        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }

    private fun isAuthRequest(request: Request): Boolean {
        val path = request.url.encodedPath
        return path.contains("/auth/token")
    }
}