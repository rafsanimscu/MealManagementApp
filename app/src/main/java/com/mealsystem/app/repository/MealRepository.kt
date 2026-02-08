package com.mealsystem.app.repository

import com.mealsystem.app.data.source.MealDataSource
import com.mealsystem.app.data.entity.*
import com.mealsystem.app.utils.SecurityUtils
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// UPGRADE: Repository now depends on an Interface, not the concrete DAO
class MealRepository(private val dataSource: MealDataSource) {

    private fun getMonthYear(timestamp: Long): String {
        val sdf = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // --- LOGIN (Security) ---
    suspend fun loginUser(email: String, password: String): UserEntity? {
        return try {
            val hash = SecurityUtils.hashPassword(password)
            dataSource.login(email, hash)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // --- RATE CALCULATION (Business Logic) ---
    suspend fun calculateMealRate(monthYear: String): Double {
        return try {
            val totalPurchases = dataSource.getTotalPurchasesByMonth(monthYear) ?: 0.0
            val totalMeals = dataSource.getTotalMealCountByMonth(monthYear) ?: 1.0
            if (totalMeals > 0) totalPurchases / totalMeals else 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    // --- ACTIONS WITH LOCK (Stability) ---
    suspend fun addMealWithLock(meal: MealLogEntity, activeMonth: String): Boolean {
        return try {
            val mealMonth = getMonthYear(meal.timestamp)
            if (mealMonth != activeMonth) throw Exception("Month Locked")
            dataSource.insertMeal(meal)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addPurchaseWithLock(purchase: PurchaseEntity, activeMonth: String): Boolean {
        return try {
            val purchaseMonth = getMonthYear(purchase.date)
            if (purchaseMonth != activeMonth) throw Exception("Month Locked")
            dataSource.insertPurchase(purchase)
            true
        } catch (e: Exception) {
            false
        }
    }
}