package com.mealsystem.app.data.dao

import androidx.room.*
import com.mealsystem.app.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealSystemDao {
    
    // --- USERS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getAllActiveUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE userId = :id")
    fun getUserById(id: Int): Flow<UserEntity?>

    // --- ROLES ---
    @Insert
    suspend fun insertRole(role: RoleEntity)

    @Query("SELECT * FROM roles")
    suspend fun getAllRoles(): List<RoleEntity>

    // --- MEAL LOGS ---
    @Insert
    suspend fun insertMealLog(log: MealLogEntity)

    @Query("SELECT * FROM meal_logs WHERE userId = :userId ORDER BY date DESC")
    fun getMealLogsByUser(userId: Int): Flow<List<MealLogEntity>>

    // --- PURCHASES ---
    @Insert
    suspend fun insertPurchase(purchase: PurchaseEntity)

    @Query("SELECT * FROM purchases ORDER BY date DESC")
    fun getAllPurchases(): Flow<List<PurchaseEntity>>

    // --- PAYMENTS ---
    @Insert
    suspend fun insertPayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE userId = :userId ORDER BY date DESC")
    fun getPaymentsByUser(userId: Int): Flow<List<PaymentEntity>>

    // --- SETTINGS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>
        // --- BUSINESS LOGIC QUERIES (Step 5) ---

    // 1. Get Total Purchases for a specific month (MM-yyyy)
    @Query("SELECT SUM(totalCost) FROM purchases WHERE strftime('%m-%Y', date/1000, 'unixepoch') = :monthYear")
    suspend fun getTotalPurchasesByMonth(monthYear: String): Double?

    // 2. Get Total Meals (Self + Guests) for a specific month
    @Query("SELECT COUNT(*) + SUM(guestCount) FROM meal_logs WHERE strftime('%m-%Y', date/1000, 'unixepoch') = :monthYear")
    suspend fun getTotalMealCountByMonth(monthYear: String): Double?

    // 3. Get Total Payments for a User in a specific month
    @Query("SELECT SUM(amount) FROM payments WHERE userId = :userId AND strftime('%m-%Y', date/1000, 'unixepoch') = :monthYear")
    suspend fun getTotalPaymentsByUser(userId: Int, monthYear: String): Double?
    
    // 4. Get User's specific meal count for a month
    @Query("SELECT COUNT(*) + SUM(guestCount) FROM meal_logs WHERE userId = :userId AND strftime('%m-%Y', date/1000, 'unixepoch') = :monthYear")
    suspend fun getUserMealCountByMonth(userId: Int, monthYear: String): Double?
}