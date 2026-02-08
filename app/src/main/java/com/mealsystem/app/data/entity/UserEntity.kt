package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val name: String,
    val email: String,
    val password: String, // In a real app, hash this!
    val role: String, // "admin" or "member"
    val balance: Double = 0.0,
    val isActive: Boolean = true
)