package com.mealsystem.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val paymentId: Int = 0,
    val userId: Int,
    val amount: Double,
    val paymentMode: String, // Cash, Bank Transfer, etc.
    val date: Long = System.currentTimeMillis()
)