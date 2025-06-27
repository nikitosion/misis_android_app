package com.example.misisapp.di

import com.example.misisapp.BuildConfig
import com.example.misisapp.data.local.CurrentUserPrefs
import com.example.misisapp.data.remote.api.AuthApi
import com.example.misisapp.data.remote.api.GroupApi
import com.example.misisapp.data.remote.api.ScheduleApi
import com.example.misisapp.data.remote.api.UserApi
import com.example.misisapp.data.remote.interceptor.AuthInterceptor
import com.example.misisapp.data.remote.interceptor.TokenAuthenticator
import com.example.misisapp.data.session.SessionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(prefs: CurrentUserPrefs): AuthInterceptor {
        return AuthInterceptor(prefs)
    }

    @Provides @Singleton
    fun provideTokenAuthenticator(
        prefs: CurrentUserPrefs,
        sessionManager: SessionManager
    ): TokenAuthenticator = TokenAuthenticator(prefs, sessionManager)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
    }

    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi = retrofit.create(ScheduleApi::class.java)

    @Provides @Singleton
    fun provideGroupApi(retrofit: Retrofit): GroupApi = retrofit.create(GroupApi::class.java)
}