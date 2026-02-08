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
}