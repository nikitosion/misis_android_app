package com.example.misisapp.data.mapper

import com.example.misisapp.data.local.entity.UserEntity
import com.example.misisapp.data.remote.dto.UserDto
import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.model.UserRole
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {

    fun mapToEntity(dto: UserDto): UserEntity {
        return UserEntity(
            id = dto.id,
            username = dto.username,
            role = dto.role,
            groupId = dto.groudId,
            fullName = dto.fullName,
            email = dto.email,
            isActive = dto.isActive,
        )
    }

    fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            role = UserRole.valueOf(entity.role),
            groupId = entity.groupId,
            fullName = entity.fullName,
            email = entity.email,
            isActive = entity.isActive,
        )
    }

    fun mapToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            username = dto.username,
            role = UserRole.valueOf(dto.role.toString().uppercase()),
            groupId = dto.groudId,
            fullName = dto.fullName,
            email = dto.email,
            isActive = dto.isActive,
        )
    }
}