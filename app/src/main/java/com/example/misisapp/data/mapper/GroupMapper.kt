package com.example.misisapp.data.mapper

import com.example.misisapp.data.local.entity.GroupEntity
import com.example.misisapp.data.remote.dto.GroupDto
import com.example.misisapp.domain.model.Group
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupMapper @Inject constructor() {
    fun fromDto(dto: GroupDto): GroupEntity = GroupEntity(dto.id, dto.name)
    fun toDomain(entity: GroupEntity): Group = Group(entity.id, entity.name)
}