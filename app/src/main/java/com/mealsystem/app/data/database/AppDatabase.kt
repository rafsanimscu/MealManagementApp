package com.mealsystem.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mealsystem.app.data.dao.UserDao
import com.mealsystem.app.data.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
