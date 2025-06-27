package com.example.misisapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.misisapp.data.local.dao.GroupDao
import com.example.misisapp.data.local.dao.ScheduleDao
import com.example.misisapp.data.local.dao.UserDao
import com.example.misisapp.data.local.entity.GroupEntity
import com.example.misisapp.data.local.entity.ScheduleEntity
import com.example.misisapp.data.local.entity.UserEntity

@Database(
    entities = [ScheduleEntity::class, UserEntity::class, GroupEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao() : ScheduleDao
    abstract fun userDao() : UserDao
    abstract fun groupDao() : GroupDao
}