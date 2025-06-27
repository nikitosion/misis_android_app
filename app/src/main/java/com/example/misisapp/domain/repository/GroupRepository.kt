package com.example.misisapp.domain.repository

import com.example.misisapp.domain.model.Group

interface GroupRepository {
    suspend fun getAllGroups(): List<Group>
    suspend fun getGroupById(id: String): Group?
}