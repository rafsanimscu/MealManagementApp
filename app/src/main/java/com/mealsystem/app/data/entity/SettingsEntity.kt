package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1, // Singleton ID (always 1)
    val costPerMeal: Double,
    val guestMultiplier: Double, // e.g., 1.5x rate for guests
    val staffCharge: Double,
    val activeMonth: String // "10-2023"
)