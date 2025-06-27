package com.example.misisapp.data.repository

import com.example.misisapp.data.local.dao.GroupDao
import com.example.misisapp.data.mapper.GroupMapper
import com.example.misisapp.data.remote.api.GroupApi
import com.example.misisapp.domain.model.Group
import com.example.misisapp.domain.repository.GroupRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val api: GroupApi,
    private val dao: GroupDao,
    private val mapper: GroupMapper
) : GroupRepository {

    override suspend fun getAllGroups(): List<Group> {
        val dtos = api.getAllGroups()
        val entities = dtos.map(mapper::fromDto)
        dao.insertAll(entities)
        return entities.map(mapper::toDomain)
    }

    override suspend fun getGroupById(id: String): Group? {
        return dao.getGroupById(id)?.let(mapper::toDomain)
    }
}