package com.mealsystem.app.repository

import com.mealsystem.app.data.dao.MealSystemDao
import com.mealsystem.app.data.entity.*
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MealRepository(private val dao: MealSystemDao) {

    private fun getMonthYear(timestamp: Long): String {
        val sdf = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // --- CRUD with Try-Catch (Crash Prevention) ---
    
    suspend fun loginUser(email: String, password: String): UserEntity? {
        return try {
            val hash = com.mealsystem.app.utils.SecurityUtils.hashPassword(password)
            dao.login(email, hash)
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null instead of crashing
        }
    }

    suspend fun calculateMealRate(monthYear: String): Double {
        return try {
            val totalPurchases = dao.getTotalPurchasesByMonth(monthYear) ?: 0.0
            val totalMeals = dao.getTotalMealCountByMonth(monthYear) ?: 1.0
            if (totalMeals > 0) totalPurchases / totalMeals else 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    suspend fun addMealWithLock(meal: MealLogEntity, activeMonth: String): Boolean {
        return try {
            val mealMonth = getMonthYear(meal.timestamp)
            if (mealMonth != activeMonth) {
                throw Exception("Month Locked")
            }
            dao.insertMealLog(meal)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addPurchaseWithLock(purchase: PurchaseEntity, activeMonth: String): Boolean {
        return try {
            val purchaseMonth = getMonthYear(purchase.date)
            if (purchaseMonth != activeMonth) throw Exception("Month Locked")
            dao.insertPurchase(purchase)
            true
        } catch (e: Exception) {
            false
        }
    }
}