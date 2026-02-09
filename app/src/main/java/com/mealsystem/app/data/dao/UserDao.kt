package com.mealsystem.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mealsystem.app.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity): Long
    
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>
}
