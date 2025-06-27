package com.example.misisapp.di

import com.example.misisapp.data.repository.AuthRepositoryImpl
import com.example.misisapp.data.repository.CurrentUserRepositoryImpl
import com.example.misisapp.data.repository.GroupRepositoryImpl
import com.example.misisapp.data.repository.ScheduleRepositoryImpl
import com.example.misisapp.data.repository.UserRepositoryImpl
import com.example.misisapp.domain.repository.AuthRepository
import com.example.misisapp.domain.repository.CurrentUserRepository
import com.example.misisapp.domain.repository.GroupRepository
import com.example.misisapp.domain.repository.ScheduleRepository
import com.example.misisapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ): ScheduleRepository

    @Binds abstract fun bindGroupRepository(
        impl: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    abstract fun bindCurrentUserRepository(
        impl: CurrentUserRepositoryImpl
    ): CurrentUserRepository
}