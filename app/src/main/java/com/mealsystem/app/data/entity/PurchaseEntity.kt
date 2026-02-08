package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true) val purchaseId: Int = 0,
    val itemName: String,
    val quantity: Double, // e.g., 5.5 KG
    val totalCost: Double,
    val date: Long,
    val addedBy: Int // Admin ID who added this
)