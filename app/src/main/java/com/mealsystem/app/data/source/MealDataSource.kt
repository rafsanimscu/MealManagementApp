package com.mealsystem.app.data.source

import com.mealsystem.app.data.entity.*

// The "Contract"
interface MealDataSource {
    
    // --- USER ---
    suspend fun login(email: String, passHash: String): UserEntity?
    suspend fun getAllActiveUsers(): List<UserEntity>
    suspend fun getUserById(id: Int): UserEntity?

    // --- MEALS ---
    suspend fun insertMeal(meal: MealLogEntity)
    suspend fun getMealLogsByUser(userId: Int): List<MealLogEntity>

    // --- EXPENSES ---
    suspend fun insertPurchase(purchase: PurchaseEntity)
    suspend fun getAllPurchases(): List<PurchaseEntity>

    // --- PAYMENTS ---
    suspend fun insertPayment(payment: PaymentEntity)
    suspend fun getPaymentsByUser(userId: Int): List<PaymentEntity>

    // --- AGGREGATE QUERIES (For Rate Calculation) ---
    suspend fun getTotalPurchasesByMonth(monthYear: String): Double?
    suspend fun getTotalMealCountByMonth(monthYear: String): Double?
    suspend fun getTotalPaymentsByUser(userId: Int, monthYear: String): Double?
    suspend fun getUserMealCountByMonth(userId: Int, monthYear: String): Double?

    // --- SETTINGS ---
    suspend fun getSettings(): SettingsEntity?
    suspend fun insertSettings(settings: SettingsEntity)
}