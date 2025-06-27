package com.example.misisapp.domain.usecase

import com.example.misisapp.domain.model.Group
import com.example.misisapp.domain.repository.GroupRepository
import javax.inject.Inject

class GetAllGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(): List<Group> = repository.getAllGroups()
}