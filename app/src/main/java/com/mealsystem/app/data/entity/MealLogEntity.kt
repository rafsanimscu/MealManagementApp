package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_logs")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Int = 0,
    val userId: Int, // FK to Users
    val date: Long, // Timestamp for specific date
    val mealType: String, // "Breakfast", "Lunch", "Dinner"
    val guestCount: Int = 0, // 0 if self meal, >0 if guest
    val appliedMealRate: Double, // Rate frozen at time of eating
    val timestamp: Long = System.currentTimeMillis()
)