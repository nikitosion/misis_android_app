package com.example.misisapp.di

import com.example.misisapp.domain.repository.AuthRepository
import com.example.misisapp.domain.repository.GroupRepository
import com.example.misisapp.domain.usecase.GetAllGroupsUseCase
import com.example.misisapp.domain.usecase.LogoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideLogoutUseCase(authRepo: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepo)
    }

    @Provides
    fun provideGetAllGroupsUseCase(repo: GroupRepository): GetAllGroupsUseCase =
        GetAllGroupsUseCase(repo)
}