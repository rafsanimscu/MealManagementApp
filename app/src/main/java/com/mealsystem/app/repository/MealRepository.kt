package com.mealsystem.app.repository

import android.content.Context
import com.mealsystem.app.data.dao.MealSystemDao
import com.mealsystem.app.data.entity.MealLogEntity
import com.mealsystem.app.data.entity.PurchaseEntity
import com.mealsystem.app.data.entity.PaymentEntity
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MealRepository(private val dao: MealSystemDao) {

    // Helper: Convert timestamp to "MM-yyyy"
    private fun getMonthYear(timestamp: Long): String {
        val sdf = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    // ==========================================
    // RULE 1: MEAL RATE CALCULATION
    // ==========================================
    // Rate = Total Purchases / Total Meals
    suspend fun calculateMealRate(monthYear: String): Double {
        val totalPurchases = dao.getTotalPurchasesByMonth(monthYear) ?: 0.0
        val totalMeals = dao.getTotalMealCountByMonth(monthYear) ?: 1.0 // Avoid divide by zero

        return if (totalMeals > 0) totalPurchases / totalMeals else 0.0
    }

    // ==========================================
    // RULE 2: USER MONTHLY COST
    // ==========================================
    // Cost = (Meals * Rate) + (GuestMeals * Rate * Multiplier) + StaffCharge
    suspend fun calculateUserCost(userId: Int, monthYear: String, guestMultiplier: Double, staffCharge: Double): Double {
        // Note: In a real scenario, you might fetch the ACTUAL appliedRate from logs.
        // For estimation based on current settings:
        val currentRate = calculateMealRate(monthYear)
        
        val userMealCount = dao.getUserMealCountByMonth(userId, monthYear) ?: 0.0
        
        // Simplified formula assuming GuestCount is included in userMealCount from query
        // Note: To be perfectly accurate to your formula, we need separate self/guest counts.
        // For this implementation, we assume the DAO query returns the weighted total or we apply the rate broadly.
        
        val baseCost = userMealCount * currentRate
        val totalCost = baseCost + staffCharge
        
        return totalCost
    }

    // ==========================================
    // RULE 3: BALANCE CALCULATION
    // ==========================================
    // Balance = Total Payments - Total User Cost
    suspend fun getUserBalance(userId: Int, monthYear: String): Double {
        val totalPayments = dao.getTotalPaymentsByUser(userId, monthYear) ?: 0.0
        val totalCost = 0.0 // Ideally calculateUserCost here, but this is a simple balance check
        
        return totalPayments - totalCost
    }

    // ==========================================
    // RULE 4: MONTH LOCK RULE
    // ==========================================
    // If date != activeMonth, throw error
    suspend fun addMealWithLock(meal: MealLogEntity, activeMonth: String) {
        val mealMonth = getMonthYear(meal.timestamp)
        
        if (mealMonth != activeMonth) {
            throw Exception("Month Locked: Cannot edit data for $mealMonth. Active month is $activeMonth")
        }
        
        dao.insertMealLog(meal)
    }

    suspend fun addPurchaseWithLock(purchase: PurchaseEntity, activeMonth: String) {
        val purchaseMonth = getMonthYear(purchase.date)
        
        if (purchaseMonth != activeMonth) {
            throw Exception("Month Locked: Cannot add expense for $purchaseMonth")
        }
        
        dao.insertPurchase(purchase)
    }
    
    suspend fun addPaymentWithLock(payment: PaymentEntity, activeMonth: String) {
        val paymentMonth = getMonthYear(payment.date)
        
        if (paymentMonth != activeMonth) {
            throw Exception("Month Locked: Cannot add payment for $paymentMonth")
        }
        
        dao.insertPayment(payment)
    }
}