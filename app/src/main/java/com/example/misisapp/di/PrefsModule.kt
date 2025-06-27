package com.example.misisapp.di

import com.example.misisapp.data.local.CurrentUserPrefs
import com.example.misisapp.data.local.CurrentUserPrefsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PrefsModule {

    @Binds
    abstract fun bindCurrentUserPrefs(impl: CurrentUserPrefsImpl): CurrentUserPrefs
}