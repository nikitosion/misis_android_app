package com.example.misisapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val role: String,
    val groupId: String,
    val fullName: String,
    val email: String,
    val isActive: Boolean
)
