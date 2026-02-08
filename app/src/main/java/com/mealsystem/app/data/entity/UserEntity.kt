package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val email: String,
    val passwordHash: String,
    val name: String,
    val roleId: Int, // References Roles Table
    val mobile: String?,
    val room: String?,
    val balance: Double = 0.0,
    val isActive: Boolean = true
)